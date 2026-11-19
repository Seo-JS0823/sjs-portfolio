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

import com.jubu.pofol.domain.request.LinkCardRequests.LinkCardCreateRequest;
import com.jubu.pofol.domain.request.LinkCardRequests.LinkCardUpdateRequest;
import com.jubu.pofol.domain.response.LinkCardResponses.LinkCardCreateResponse;
import com.jubu.pofol.domain.response.LinkCardResponses.LinkCardListResponse;
import com.jubu.pofol.domain.response.LinkCardResponses.LinkCardUpdateResponse;
import com.jubu.pofol.service.LinkCardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/linkcard")
@RequiredArgsConstructor
public class LinkCardController {

	private final LinkCardService lcService;
	
	@PostMapping
	public ResponseEntity<LinkCardCreateResponse> create(
			@RequestBody LinkCardCreateRequest request
	) {
		LinkCardCreateResponse response = lcService.create(request);
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping
	public ResponseEntity<LinkCardListResponse> getList() {
		LinkCardListResponse response = lcService.getList();
		
		return ResponseEntity.ok(response);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<LinkCardUpdateResponse> update(
			@RequestBody LinkCardUpdateRequest request,
			@PathVariable("id") Long linkCardId
	) {
		LinkCardUpdateResponse response = lcService.update(linkCardId, request);
		
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(
			@PathVariable("id") Long linkCardId
	) {
		lcService.delete(linkCardId);
		
		return ResponseEntity.noContent().build();
	}
}
