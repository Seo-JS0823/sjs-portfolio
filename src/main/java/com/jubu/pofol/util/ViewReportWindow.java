package com.jubu.pofol.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

public final class ViewReportWindow {

	public static final ZoneId ZONE = ZoneId.of("Asia/Seoul");
	public static final LocalTime CUTOFF = LocalTime.of(18, 0);

	private ViewReportWindow() {}

	public static LocalDate reportDateOf(LocalDateTime time) {
		LocalDate date = time.toLocalDate();

		return time.toLocalTime().isBefore(CUTOFF) ? date : date.plusDays(1);
	}

	public static LocalDate currentReportDate() {
		return reportDateOf(LocalDateTime.now(ZONE));
	}

	public static LocalDateTime startOf(LocalDate reportDate) {
		return reportDate.minusDays(1).atTime(CUTOFF);
	}

	public static LocalDateTime endOf(LocalDate reportDate) {
		return reportDate.atTime(CUTOFF).minusMinutes(1);
	}
}
