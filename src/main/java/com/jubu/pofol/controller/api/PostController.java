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

import com.jubu.pofol.domain.request.PostRequests.PostCreateRequest;
import com.jubu.pofol.domain.request.PostRequests.PostUpdateRequest;
import com.jubu.pofol.domain.response.PostResponses.PostCreateResponse;
import com.jubu.pofol.domain.response.PostResponses.PostDetailResponse;
import com.jubu.pofol.domain.response.PostResponses.PostListResponse;
import com.jubu.pofol.domain.response.PostResponses.PostUpdateResponse;
import com.jubu.pofol.service.PostService;
import com.jubu.pofol.service.PostViewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

	private final PostService postService;
	private final PostViewService postViewService;
	
	@PostMapping
	public ResponseEntity<PostCreateResponse> create(
			@RequestBody PostCreateRequest request
	) {
		PostCreateResponse response = postService.create(request);
		
		return ResponseEntity.ok(response);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<PostUpdateResponse> update(
			@RequestBody PostUpdateRequest request,
			@PathVariable("id") Long postId
	) {
		PostUpdateResponse response = postService.update(postId, request);
		
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(
			@PathVariable("id") Long postId
	) {
		postService.delete(postId);
		
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PostDetailResponse> getOne(
			@PathVariable("id") Long postId
	) {
		PostDetailResponse response = postService.getDetail(postId);
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/{id}/views")
	public ResponseEntity<Void> increaseView(
			@PathVariable("id") Long postId
	) {
		postViewService.increase(postId);

		return ResponseEntity.noContent().build();
	}

	@GetMapping
	public ResponseEntity<PostListResponse> getList() {
		PostListResponse response = postService.getList();
		
		return ResponseEntity.ok(response);
	}
}
