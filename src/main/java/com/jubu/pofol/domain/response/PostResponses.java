package com.jubu.pofol.domain.response;

import java.util.List;

import com.jubu.pofol.domain.model.Post;
import com.jubu.pofol.domain.model.PostLink;
import com.jubu.pofol.util.Markdown;
import com.jubu.pofol.util.YmdDate;

public final class PostResponses {

	public record PostDetailResponse(
			String title,
			String description,
			String content,
			String contentHtml,
			String startDate,
			String endDate,
			List<PostLinkItem> links,
			long viewCount
	) {
		public static PostDetailResponse of(Post post) {
			return new PostDetailResponse(
					post.getTitle(),
					post.getDescription(),
					post.getContent(),
					Markdown.toHtml(post.getContent()),
					YmdDate.format(post.getStartDate()),
					YmdDate.format(post.getEndDate()),
					PostLinkItem.listOf(post),
					post.getViewCount()
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

	public record PostSummary(
			String title,
			String description,
			Long postId,
			String startDate,
			String endDate,
			List<PostLinkItem> links,
			long viewCount
	) {
		public static PostSummary of(Post post) {
			return new PostSummary(
					post.getTitle(),
					post.getDescription(),
					post.getId(),
					YmdDate.format(post.getStartDate()),
					YmdDate.format(post.getEndDate()),
					PostLinkItem.listOf(post),
					post.getViewCount()
			);
		}
	}

	public record PostLinkItem(Long postLinkId, String title, String url) {
		public static PostLinkItem of(PostLink link) {
			return new PostLinkItem(link.getId(), link.getTitle(), link.getUrl());
		}

		public static List<PostLinkItem> listOf(Post post) {
			return post.getLinks().stream()
					.map(PostLinkItem::of)
					.toList();
		}
	}

	public record PostCreateResponse(
			String title,
			String description,
			String content,
			Long postId,
			String startDate,
			String endDate,
			List<PostLinkItem> links
	) {
		public static PostCreateResponse of(Post post) {
			return new PostCreateResponse(
					post.getTitle(),
					post.getDescription(),
					post.getContent(),
					post.getId(),
					YmdDate.format(post.getStartDate()),
					YmdDate.format(post.getEndDate()),
					PostLinkItem.listOf(post)
			);
		}
	}

	public record PostUpdateResponse(
			String title,
			String description,
			String content,
			Long postId,
			String startDate,
			String endDate,
			List<PostLinkItem> links
	) {
		public static PostUpdateResponse of(Post post) {
			return new PostUpdateResponse(
					post.getTitle(),
					post.getDescription(),
					post.getContent(),
					post.getId(),
					YmdDate.format(post.getStartDate()),
					YmdDate.format(post.getEndDate()),
					PostLinkItem.listOf(post)
			);
		}
	}

}
