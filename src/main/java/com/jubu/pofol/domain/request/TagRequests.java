package com.jubu.pofol.domain.request;

public final class TagRequests {

	// TagGroup
	public record TagGroupCreateRequest(String name) {
		
	}
	
	public record TagGroupUpdateRequest(String name) {
		
	}
	
	public record TagCreateRequest(String tagName, String bgColor, String tColor, Long tagGroupId) {
		
	}
	
	public record TagUpdateRequest(String tagName, String bgColor, String tColor) {
		public boolean hasName() {
			return tagName == null;
		}
		
		public boolean hasBgColor() {
			return bgColor == null;
		}
		
		public boolean hasTColor() {
			return tColor == null;
		}
	}
}
