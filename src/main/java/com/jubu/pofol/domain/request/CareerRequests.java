package com.jubu.pofol.domain.request;

import java.util.List;

public final class CareerRequests {

	public record CareerCreateRequest(
			String title,
			String startDate,
			String endDate,
			List<String> keywords,
			List<String> contents
	) {

	}

	public record CareerUpdateRequest(
			String title,
			String startDate,
			String endDate,
			List<String> keywords,
			List<String> contents
	) {
		public boolean hasTitle() {
			return title == null;
		}

		public boolean hasStartDate() {
			return startDate == null;
		}

		public boolean hasEndDate() {
			return endDate == null;
		}

		public boolean hasKeywords() {
			return keywords == null;
		}

		public boolean hasContents() {
			return contents == null;
		}
	}

}
