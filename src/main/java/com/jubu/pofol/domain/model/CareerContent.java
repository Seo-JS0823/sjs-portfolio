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
@Table(name = "career_contents")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CareerContent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "career_id", nullable = false)
	private Career career;

	static CareerContent create(String content, Career career) {
		Check.notBlank(content, () -> new CustomException("career content is null"));
		Check.notNull(career, () -> new CustomException("career is null"));

		CareerContent careerContent = new CareerContent();

		careerContent.content = content.strip();
		careerContent.career = career;

		return careerContent;
	}
}
