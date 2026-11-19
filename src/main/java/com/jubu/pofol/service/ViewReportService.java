package com.jubu.pofol.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jubu.pofol.config.AdminProperties;
import com.jubu.pofol.domain.model.Post;
import com.jubu.pofol.domain.model.PostDailyView;
import com.jubu.pofol.repository.PostDailyViewRepo;
import com.jubu.pofol.repository.PostRepo;
import com.jubu.pofol.util.ViewReportWindow;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ViewReportService {

	private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	private final PostRepo postRepo;
	private final PostDailyViewRepo dailyViewRepo;
	private final MailSenderService mailSender;
	private final AdminProperties adminProperties;

	@Transactional(readOnly = true)
	public void send(LocalDate reportDate) {
		String email = adminProperties.email();

		if (email == null || email.isBlank()) {
			log.warn("관리자 이메일이 설정되지 않아 조회수 리포트를 보내지 않습니다.");
			return;
		}

		mailSender.send(
				email,
				"[pofol] " + reportDate.format(DATE) + " 프로젝트 조회수",
				buildBody(reportDate)
		);
	}

	@Transactional(readOnly = true)
	public String buildBody(LocalDate reportDate) {
		List<Post> posts = postRepo.findAll(Sort.by("id"));

		Map<Long, Long> counts = dailyViewRepo.findByReportDate(reportDate).stream()
				.collect(Collectors.toMap(view -> view.getPost().getId(), PostDailyView::getViewCount));

		StringBuilder body = new StringBuilder()
				.append("날짜: ").append(reportDate.format(DATE)).append('\n')
				.append("집계 기간: ")
				.append(ViewReportWindow.startOf(reportDate).format(DATE_TIME))
				.append(" ~ ")
				.append(ViewReportWindow.endOf(reportDate).format(DATE_TIME))
				.append("\n\n");

		if (posts.isEmpty()) {
			return body.append("등록된 프로젝트가 없습니다.\n").toString();
		}

		long total = 0;

		for (Post post : posts) {
			long count = counts.getOrDefault(post.getId(), 0L);
			total += count;
			body.append("- ").append(post.getTitle()).append(" [조회수: ").append(count).append("]\n");
		}

		return body.append("\n합계 [조회수: ").append(total).append("]\n").toString();
	}
}
