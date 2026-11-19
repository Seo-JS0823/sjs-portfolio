package com.jubu.pofol.domain.model;

import com.jubu.pofol.domain.CustomException;
import com.jubu.pofol.util.Check;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post_links")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PostLink {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String title;

	@Column(nullable = false, length = 1000)
	private String url;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", nullable = false)
	private Post post;

	static PostLink create(String title, String url, Post post) {
		Check.notBlank(title, () -> new CustomException("post link title is null"));
		Check.notBlank(url, () -> new CustomException("post link url is null"));
		Check.notNull(post, () -> new CustomException("post is null"));

		String trimmedUrl = url.strip();

		if (!trimmedUrl.matches("(?i)^https?://.+")) {
			throw new CustomException("post link url must start with http:// or https://");
		}

		PostLink link = new PostLink();

		link.title = title.strip();
		link.url = trimmedUrl;
		link.post = post;

		return link;
	}
}
