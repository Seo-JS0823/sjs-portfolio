package com.jubu.pofol.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jubu.pofol.domain.model.Post;
import com.jubu.pofol.domain.model.PostContent;
import com.jubu.pofol.domain.request.PostRequests.PostCreateRequest;
import com.jubu.pofol.domain.request.PostRequests.PostUpdateRequest;
import com.jubu.pofol.domain.response.PostResponses.PostCreateResponse;
import com.jubu.pofol.domain.response.PostResponses.PostDetailResponse;
import com.jubu.pofol.domain.response.PostResponses.PostListResponse;
import com.jubu.pofol.domain.response.PostResponses.PostUpdateResponse;
import com.jubu.pofol.repository.PostRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepo postRepo;
	
	@Transactional(readOnly = true)
	public PostDetailResponse getDetail(Long postId) {
		Post post = postRepo.findByIdOrThrow(postId);
		
		return PostDetailResponse.of(post);
	}
	
	@Transactional(readOnly = true)
	public PostListResponse getList() {
		List<Post> posts = postRepo.findAll();
		
		return PostListResponse.of(posts);
	}
	
	@Transactional
	public PostCreateResponse create(PostCreateRequest request) {
		Post post = Post.create(request.title(), request.description());
		
		PostContent content = PostContent.create(request.content(), post);
		
		post.addContent(content);
		
		postRepo.save(post);
		
		return new PostCreateResponse(
				post.getTitle(),
				post.getDescription(),
				post.getContent(),
				post.getId()
		);
	}

	@Transactional
	public PostUpdateResponse update(Long postId, PostUpdateRequest request) {
		Post post = postRepo.findByIdOrThrow(postId);
		
		if(!request.hasTitle()) {
			post.changeTitle(request.title());
		}
		
		if(!request.hasDescription()) {
			post.changeDescription(request.description());
		}
		
		if(!request.hasContent()) {
			post.changeContent(request.content());
		}
		
		return new PostUpdateResponse(
				post.getTitle(),
				post.getDescription(),
				post.getContent(),
				post.getId()
		);
	}

	public void delete(Long postId) {
		Post post = postRepo.findByIdOrThrow(postId);
		
		postRepo.delete(post);
	}
	
}
