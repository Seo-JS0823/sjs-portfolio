package com.jubu.pofol.domain.request;

public final class LinkCardRequests {

	public record LinkCardCreateRequest(String url, String title, String description) {
		
	}
	
	public record LinkCardUpdateRequest(String url, String title, String description) {
		
	}
}
