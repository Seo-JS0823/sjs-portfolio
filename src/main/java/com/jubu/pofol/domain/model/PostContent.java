package com.jubu.pofol.domain.model;

import com.jubu.pofol.domain.CustomException;
import com.jubu.pofol.util.Check;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contents")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PostContent {

	@Id
	private Long id;
	
	@MapsId
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;
	
	@Column(nullable = false, columnDefinition = "TEXT")
	private String content;
	
	public static PostContent create(String content, Post post) {
		Check.notBlank(content, () -> new CustomException("post content is null"));
		Check.notNull(post, () -> new CustomException("post is null"));
		
		PostContent postContent = new PostContent();
		
		postContent.content = content;
		postContent.post = post;
		
		return postContent;
	}
	
	void changeContent(String content) {
		Check.notBlank(content, () -> new CustomException("post content is null"));
		
		this.content = content;
	}
}
