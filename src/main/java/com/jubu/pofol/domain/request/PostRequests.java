package com.jubu.pofol.domain.request;

public final class PostRequests {

	public record PostCreateRequest(String title, String description, String content) {
		
	}
	
	public record PostUpdateRequest(String title, String description, String content) {
		public boolean hasTitle() {
			return title == null;
		}
		
		public boolean hasDescription() {
			return description == null;
		}
		
		public boolean hasContent() {
			return content == null;
		}
	}
	
}
