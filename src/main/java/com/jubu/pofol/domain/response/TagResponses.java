package com.jubu.pofol.domain.response;

import java.util.List;

import com.jubu.pofol.domain.model.Tag;
import com.jubu.pofol.domain.model.TagGroup;

public final class TagResponses {

	public record TagGroupCreateResponse(Long tagGroupId, String tagGroupName) {
		
	}
	
	public record TagSummaryResponse(List<TagSummary> tags) {
		
	}
	
	public record TagSummary(Long tagGroupId, String tagGroupName, List<TagItem> tagItems) {
		public static TagSummary of(TagGroup tagGroup, List<TagItem> tagItems) {
			return new TagSummary(
					tagGroup.getId(),
					tagGroup.getName(),
					tagItems
			);
		}
	}
	
	public record TagItem(Long tagId, String tagName, String bgColor, String tColor) {
		public static TagItem of(Tag tag) {
			return new TagItem(
					tag.getId(),
					tag.getName(),
					tag.getBgColor(),
					tag.getTColor()
			);
		}
	}
	
	public record TagCreateResponse(Long tagId, String tagName, String bgColor, String tColor) {
		
	}
	
	public record TagUpdateResponse(Long tagId, String tagName, String bgColor, String tColor) {
		
	}
}
