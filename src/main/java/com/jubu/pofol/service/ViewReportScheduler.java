package com.jubu.pofol.service;

import java.time.LocalDate;

import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jubu.pofol.util.ViewReportWindow;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ViewReportScheduler {

	private final ViewReportService viewReportService;

	@Scheduled(cron = "${view-report.cron:0 0 18 * * *}", zone = "Asia/Seoul")
	public void sendDailyReport() {
		LocalDate reportDate = LocalDate.now(ViewReportWindow.ZONE);

		try {
			viewReportService.send(reportDate);
			log.info("조회수 리포트 전송 완료: {}", reportDate);
		} catch (MailException e) {
			log.error("조회수 리포트 메일 전송 실패: {}", reportDate, e);
		}
	}
}
