package com.jubu.pofol.domain.request;

public final class LinkCardRequests {

	public record LinkCardCreateRequest(String url, String title, String description, String icon) {

	}

	public record LinkCardUpdateRequest(String url, String title, String description, String icon) {
		public boolean hasUrl() {
			return url == null;
		}

		public boolean hasTitle() {
			return title == null;
		}

		public boolean hasDescription() {
			return description == null;
		}

		public boolean hasIcon() {
			return icon == null;
		}
	}
}
