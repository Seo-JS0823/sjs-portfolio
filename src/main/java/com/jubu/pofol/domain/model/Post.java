package com.jubu.pofol.domain.model;

import java.time.Instant;

import com.jubu.pofol.domain.CustomException;
import com.jubu.pofol.util.Check;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 100)
	private String title;
	
	@Column(nullable = false, columnDefinition = "TEXT")
	private String description;
	
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;
	
	@OneToOne(
			mappedBy = "post",
			cascade = CascadeType.ALL,
			orphanRemoval = true,
			fetch = FetchType.LAZY
	)
	private PostContent content;
	
	public static Post create(String title, String description) {
		Check.notBlank(title, () -> new CustomException("post title is null"));
		
		Check.notBlank(description, () -> new CustomException("post description is null"));
		
		Post entity = new Post();
		
		entity.title = title;
		entity.description = description;
		entity.createdAt = Instant.now();
		
		return entity;
	}
	
	public void addContent(PostContent content) {
		if (this.content != null) {
			throw new CustomException("post content not is null");
		}
		
		this.content = content;
	}
	
	public void changeTitle(String title) {
		Check.notBlank(title, () -> new CustomException("post title is null"));
		
		this.title = title;
	}
	
	public void changeDescription(String description) {
		Check.notBlank(description, () -> new CustomException("post description is null"));
		
		this.description = description;
	}
	
	public void changeContent(String content) {
		this.content.changeContent(content);
	}
	
	public String getContent() {
		return this.content.getContent();
	}
	
}
