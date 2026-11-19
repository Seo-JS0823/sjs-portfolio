package com.jubu.pofol.domain.response;

import java.util.List;

import com.jubu.pofol.domain.model.LinkCard;

public final class LinkCardResponses {

	public record LinkCardCreateResponse(Long linkCardId, String url, String title, String description, String icon) {

	}

	public record LinkCardListResponse(List<LinkCardItem> lcs) {

	}

	public record LinkCardItem(Long linkCardId, String url, String title, String description, String icon) {
		public static LinkCardItem of(LinkCard lc) {
			return new LinkCardItem(
					lc.getId(),
					lc.getUrl(),
					lc.getTitle(),
					lc.getDescription(),
					lc.getIcon()
			);
		}
	}

	public record LinkCardUpdateResponse(Long linkCardId, String url, String title, String description, String icon) {

	}
}
