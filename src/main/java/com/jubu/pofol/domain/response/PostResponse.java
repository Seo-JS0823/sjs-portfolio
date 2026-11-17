package com.jubu.pofol.domain.response;

import java.util.List;

import com.jubu.pofol.domain.model.Post;

public final class PostResponse {

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
	
	private record PostSummary(String title, String description) {
		public static PostSummary of(Post post) {
			return new PostSummary(post.getTitle(), post.getDescription());
		}
	}
	
}
