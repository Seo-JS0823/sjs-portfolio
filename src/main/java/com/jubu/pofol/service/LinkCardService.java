package com.jubu.pofol.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jubu.pofol.domain.model.LinkCard;
import com.jubu.pofol.domain.request.LinkCardRequests.LinkCardCreateRequest;
import com.jubu.pofol.domain.request.LinkCardRequests.LinkCardUpdateRequest;
import com.jubu.pofol.domain.response.LinkCardResponses.LinkCardCreateResponse;
import com.jubu.pofol.domain.response.LinkCardResponses.LinkCardItem;
import com.jubu.pofol.domain.response.LinkCardResponses.LinkCardListResponse;
import com.jubu.pofol.domain.response.LinkCardResponses.LinkCardUpdateResponse;
import com.jubu.pofol.repository.LinkCardRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LinkCardService {

	private final LinkCardRepo lcRepo;

	@Transactional
	public LinkCardCreateResponse create(LinkCardCreateRequest request) {
		LinkCard lc = LinkCard.create(
				request.url(),
				request.title(),
				request.description(),
				request.icon()
		);

		lcRepo.save(lc);

		return new LinkCardCreateResponse(
				lc.getId(),
				lc.getUrl(),
				lc.getTitle(),
				lc.getDescription(),
				lc.getIcon()
		);
	}
	
	@Transactional(readOnly = true)
	public LinkCardListResponse getList() {
		List<LinkCardItem> lcs = lcRepo.findAll().stream()
				.map(LinkCardItem::of)
				.toList();
		
		return new LinkCardListResponse(lcs);
	}
	
	@Transactional
	public LinkCardUpdateResponse update(Long linkCardId, LinkCardUpdateRequest request) {
		LinkCard lc = lcRepo.findByIdOrThrow(linkCardId);
		
		if (!request.hasUrl()) {
			lc.changeUrl(request.url());
		}
		
		if (!request.hasTitle()) {
			lc.changeTitle(request.title());
		}
		
		if (!request.hasDescription()) {
			lc.changeDescription(request.description());
		}

		if (!request.hasIcon()) {
			lc.changeIcon(request.icon());
		}

		return new LinkCardUpdateResponse(
				lc.getId(),
				lc.getUrl(),
				lc.getTitle(),
				lc.getDescription(),
				lc.getIcon()
		);
	}
	
	@Transactional
	public void delete(Long linkCardId) {
		LinkCard lc = lcRepo.findByIdOrThrow(linkCardId);
		
		lcRepo.delete(lc);
	}
}
