package com.jubu.pofol.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jubu.pofol.domain.request.TagRequests.TagCreateRequest;
import com.jubu.pofol.domain.request.TagRequests.TagGroupCreateRequest;
import com.jubu.pofol.domain.request.TagRequests.TagUpdateRequest;
import com.jubu.pofol.domain.response.TagResponses.TagCreateResponse;
import com.jubu.pofol.domain.response.TagResponses.TagGroupCreateResponse;
import com.jubu.pofol.domain.response.TagResponses.TagSummaryResponse;
import com.jubu.pofol.domain.response.TagResponses.TagUpdateResponse;
import com.jubu.pofol.service.TagService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

	private final TagService tagService;
	
	@PostMapping("/group")
	public ResponseEntity<TagGroupCreateResponse> createTagGroup(
			@RequestBody TagGroupCreateRequest request
	) {
		TagGroupCreateResponse response = tagService.createTagGroup(request);
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping
	public ResponseEntity<TagSummaryResponse> getTagSummary() {
		TagSummaryResponse response = tagService.getTagSummary();
		
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/group/{id}")
	public ResponseEntity<Void> deleteTagGroup(
			@PathVariable("id") Long tagGroupId
	) {
		tagService.deleteTagGroup(tagGroupId);
		
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping
	public ResponseEntity<TagCreateResponse> createTag(
			@RequestBody TagCreateRequest request
	) {
		TagCreateResponse response = tagService.createTag(request);
		
		return ResponseEntity.ok(response);
	}
	
	@PatchMapping("/tag/{id}")
	public ResponseEntity<TagUpdateResponse> updateTag(
			@RequestBody TagUpdateRequest request,
			@PathVariable("id") Long tagId
	) {
		TagUpdateResponse response = tagService.updateTag(tagId, request);
		
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/tag/{id}")
	public ResponseEntity<Void> deleteTag(
			@PathVariable("id") Long tagId
	) {
		tagService.deleteTag(tagId);
		
		return ResponseEntity.noContent().build();
	}
}
