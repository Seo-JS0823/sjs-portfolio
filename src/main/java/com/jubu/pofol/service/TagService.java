package com.jubu.pofol.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jubu.pofol.domain.model.Tag;
import com.jubu.pofol.domain.model.TagGroup;
import com.jubu.pofol.domain.request.TagRequests.TagCreateRequest;
import com.jubu.pofol.domain.request.TagRequests.TagGroupCreateRequest;
import com.jubu.pofol.domain.request.TagRequests.TagUpdateRequest;
import com.jubu.pofol.domain.response.TagResponses.TagCreateResponse;
import com.jubu.pofol.domain.response.TagResponses.TagGroupCreateResponse;
import com.jubu.pofol.domain.response.TagResponses.TagItem;
import com.jubu.pofol.domain.response.TagResponses.TagSummary;
import com.jubu.pofol.domain.response.TagResponses.TagSummaryResponse;
import com.jubu.pofol.domain.response.TagResponses.TagUpdateResponse;
import com.jubu.pofol.repository.TagGroupRepo;
import com.jubu.pofol.repository.TagRepo;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagService {

	private final TagRepo tagRepo;
	
	private final TagGroupRepo tagGroupRepo;

	private final EntityManager em;
	
	@Transactional
	public TagGroupCreateResponse createTagGroup(TagGroupCreateRequest request) {
		TagGroup tagGroup = TagGroup.create(request.name());
		
		tagGroupRepo.save(tagGroup);
		
		return new TagGroupCreateResponse(tagGroup.getId(), tagGroup.getName());
	}
	
	@Transactional
	public void deleteTagGroup(Long tagGroupId) {
		TagGroup tagGroup = tagGroupRepo.findByIdOrThrow(tagGroupId);
		
		List<Long> tags = tagRepo.findByTagGroup(tagGroup);
		
		tagRepo.deleteAllByIdInBatch(tags);
		
		em.flush();
		em.clear();
		
		tagGroupRepo.delete(tagGroup);
	}
	
	@Transactional(readOnly = true)
	public TagSummaryResponse getTagSummary() {
		List<TagGroup> tagGroups = tagGroupRepo.findAll();
		
		List<Tag> tags = tagRepo.findAll();
		
		Map<Long, List<TagItem>> tagItemMap = tags.stream()
				.collect(Collectors.groupingBy(
						tag -> tag.getTagGroup().getId(),
						Collectors.mapping(TagItem::of, Collectors.toList())
				));
		
		List<TagSummary> summary = tagGroups.stream()
				.map(gr -> {
					List<TagItem> items = tagItemMap.getOrDefault(gr.getId(), Collections.emptyList());
					
					return TagSummary.of(gr, items);
				})
				.toList();
		
		return new TagSummaryResponse(summary);
	}

	@Transactional
	public TagCreateResponse createTag(TagCreateRequest request) {
		TagGroup tagGroup = tagGroupRepo.findByIdOrThrow(request.tagGroupId());
		
		Tag tag = Tag.create(
				request.tagName(),
				request.bgColor(),
				request.tColor(),
				tagGroup
		);
		
		tagRepo.save(tag);
		
		return new TagCreateResponse(
				tag.getId(),
				tag.getName(),
				tag.getBgColor(),
				tag.getTColor()
		);
	}

	@Transactional
	public TagUpdateResponse updateTag(Long tagId, TagUpdateRequest request) {
		Tag tag = tagRepo.findByIdOrThrow(tagId);
		
		if (!request.hasName()) {
			tag.changeName(request.tagName());
		}
		
		if (!request.hasBgColor()) {
			tag.changeBgColor(request.bgColor());
		}
		
		if (!request.hasTColor()) {
			tag.changeTColor(request.tColor());
		}
		
		return new TagUpdateResponse(
				tag.getId(),
				tag.getName(),
				tag.getBgColor(),
				tag.getTColor()
		);
	}

	@Transactional
	public void deleteTag(Long tagId) {
		Tag tag = tagRepo.findByIdOrThrow(tagId);
		
		tagRepo.delete(tag);
	}
}
