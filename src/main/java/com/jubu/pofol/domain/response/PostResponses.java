package com.jubu.pofol.domain.response;

import java.util.List;

import com.jubu.pofol.domain.model.Post;

public final class PostResponses {

	public record PostDetailResponse(String title, String description, String content) {
		public static PostDetailResponse of(Post post) {
			return new PostDetailResponse(
					post.getTitle(),
					post.getDescription(),
					post.getContent()
			);
		}
	}
	
	public record PostListResponse(List<PostSummary> posts) {
		public static PostListResponse of(List<Post> posts) {
			List<PostSummary> summary = posts.stream()
					.map(PostSummary::of)
					.toList();
			
			return new PostListResponse(summary);
		}
	}
	
	private record PostSummary(String title, String description, Long postId) {
		public static PostSummary of(Post post) {
			return new PostSummary(post.getTitle(), post.getDescription(), post.getId());
		}
	}
	
	public record PostCreateResponse(String title, String description, String content, Long postId) {
		
	}
	
	public record PostUpdateResponse(String title, String description, String content, Long postId) {
		
	}
	
}
