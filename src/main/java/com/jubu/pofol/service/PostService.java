package com.jubu.pofol.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jubu.pofol.domain.model.Post;
import com.jubu.pofol.domain.response.PostResponse.PostDetailResponse;
import com.jubu.pofol.domain.response.PostResponse.PostListResponse;
import com.jubu.pofol.repository.PostRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepo postRepo;
	
	@Transactional(readOnly = true)
	public PostDetailResponse getDetail(Long postId) {
		Post post = postRepo.findByIdThrow(postId);
		
		return PostDetailResponse.of(post);
	}
	
	@Transactional(readOnly = true)
	public PostListResponse getList() {
		List<Post> posts = postRepo.findAll();
		
		return PostListResponse.of(posts);
	}
	
}
