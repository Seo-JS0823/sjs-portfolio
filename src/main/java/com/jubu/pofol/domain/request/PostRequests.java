package com.jubu.pofol.domain.request;

import java.util.List;

public final class PostRequests {

	public record PostCreateRequest(
			String title,
			String description,
			String content,
			String startDate,
			String endDate,
			List<PostLinkRequest> links
	) {

	}

	public record PostUpdateRequest(
			String title,
			String description,
			String content,
			String startDate,
			String endDate,
			List<PostLinkRequest> links
	) {
		public boolean hasTitle() {
			return title == null;
		}

		public boolean hasDescription() {
			return description == null;
		}

		public boolean hasContent() {
			return content == null;
		}

		public boolean hasStartDate() {
			return startDate == null;
		}

		public boolean hasEndDate() {
			return endDate == null;
		}

		public boolean hasLinks() {
			return links == null;
		}
	}

	public record PostLinkRequest(String title, String url) {

	}

}
