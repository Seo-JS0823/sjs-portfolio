package com.jubu.pofol.domain.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;

import com.jubu.pofol.domain.CustomException;
import com.jubu.pofol.util.Check;
import com.jubu.pofol.util.YmdDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
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
	
	@Column(name = "start_date", length = 8)
	private String startDate;

	@Column(name = "end_date", length = 8)
	private String endDate;

	@Column(name = "view_count", nullable = false)
	@ColumnDefault("0")
	private long viewCount;

	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;
	
	@OneToOne(
			mappedBy = "post",
			cascade = CascadeType.ALL,
			orphanRemoval = true,
			fetch = FetchType.LAZY
	)
	private PostContent content;

	@OneToMany(
			mappedBy = "post",
			cascade = CascadeType.ALL,
			orphanRemoval = true
	)
	@OrderBy("id ASC")
	@BatchSize(size = 100)
	private List<PostLink> links = new ArrayList<>();

	public static Post create(String title, String description, String startDate, String endDate) {
		Check.notBlank(title, () -> new CustomException("post title is null"));

		Check.notBlank(description, () -> new CustomException("post description is null"));

		Post entity = new Post();

		entity.title = title;
		entity.description = description;
		entity.changePeriod(startDate, endDate);
		entity.createdAt = Instant.now();

		return entity;
	}

	public void changePeriod(String startDate, String endDate) {
		String start = blankToNull(startDate);
		String end = blankToNull(endDate);

		if (start != null && !YmdDate.isValid(start)) {
			throw new CustomException("post start date is invalid");
		}

		if (end != null && !YmdDate.isValid(end)) {
			throw new CustomException("post end date is invalid");
		}

		if (start == null && end != null) {
			throw new CustomException("post start date is null");
		}

		if (start != null && end != null && start.compareTo(end) > 0) {
			throw new CustomException("post end date is before start date");
		}

		this.startDate = start;
		this.endDate = end;
	}

	private static String blankToNull(String value) {
		return (value == null || value.isBlank()) ? null : value.strip();
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

	public void increaseViewCount() {
		this.viewCount++;
	}

	public void addLink(String title, String url) {
		this.links.add(PostLink.create(title, url, this));
	}

	public void clearLinks() {
		this.links.clear();
	}

	public List<PostLink> getLinks() {
		return Collections.unmodifiableList(this.links);
	}
	
}
