package com.jubu.pofol.domain.request;

public final class TagRequests {

	// TagGroup
	public record TagGroupCreateRequest(String name) {
		
	}
	
	public record TagGroupUpdateRequest(String name) {
		
	}
	
	public record TagCreateRequest(String name, String bgColor, String tColor, Long tagGroupId) {
		
	}
	
	public record TagUpdateRequest(String name, String bgColor, String tColor) {
		
	}
}
