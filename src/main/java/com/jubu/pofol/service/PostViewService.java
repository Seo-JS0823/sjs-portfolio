package com.jubu.pofol.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jubu.pofol.domain.model.Post;
import com.jubu.pofol.domain.model.PostDailyView;
import com.jubu.pofol.repository.PostDailyViewRepo;
import com.jubu.pofol.repository.PostRepo;
import com.jubu.pofol.util.ViewReportWindow;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostViewService {

	private final PostRepo postRepo;
	private final PostDailyViewRepo dailyViewRepo;

	@Transactional
	public void increase(Long postId) {
		Post post = postRepo.findByIdOrThrow(postId);
		LocalDate reportDate = ViewReportWindow.currentReportDate();

		PostDailyView dailyView = dailyViewRepo.findByPostAndReportDate(post, reportDate)
				.orElseGet(() -> dailyViewRepo.save(PostDailyView.create(post, reportDate)));

		post.increaseViewCount();
		dailyView.increase();
	}
}
