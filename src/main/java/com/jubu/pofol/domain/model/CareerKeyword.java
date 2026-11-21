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
@Table(name = "career_keywords")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CareerKeyword {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 50)
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "career_id", nullable = false)
	private Career career;

	static CareerKeyword create(String name, Career career) {
		Check.notBlank(name, () -> new CustomException("career keyword is null"));
		Check.notNull(career, () -> new CustomException("career is null"));

		CareerKeyword keyword = new CareerKeyword();

		keyword.name = name.strip();
		keyword.career = career;

		return keyword;
	}
}
