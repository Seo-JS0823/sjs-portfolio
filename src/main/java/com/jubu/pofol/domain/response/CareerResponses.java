package com.jubu.pofol.domain.response;

import java.util.List;

import com.jubu.pofol.domain.model.Career;
import com.jubu.pofol.domain.model.CareerContent;
import com.jubu.pofol.domain.model.CareerKeyword;
import com.jubu.pofol.util.YmdDate;

public final class CareerResponses {

	public record CareerResponse(
			Long careerId,
			String title,
			String startDate,
			String endDate,
			List<String> keywords,
			List<String> contents
	) {
		public static CareerResponse of(Career career) {
			return new CareerResponse(
					career.getId(),
					career.getTitle(),
					YmdDate.format(career.getStartDate()),
					YmdDate.format(career.getEndDate()),
					career.getKeywords().stream().map(CareerKeyword::getName).toList(),
					career.getContents().stream().map(CareerContent::getContent).toList()
			);
		}
	}

	public record CareerListResponse(List<CareerResponse> careers) {
		public static CareerListResponse of(List<Career> careers) {
			return new CareerListResponse(
					careers.stream()
							.map(CareerResponse::of)
							.toList()
			);
		}
	}

}
