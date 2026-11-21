package com.jubu.pofol.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.annotations.BatchSize;

import com.jubu.pofol.domain.CustomException;
import com.jubu.pofol.util.Check;
import com.jubu.pofol.util.YmdDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "careers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Career {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String title;

	@Column(name = "start_date", nullable = false, length = 8)
	private String startDate;

	@Column(name = "end_date", length = 8)
	private String endDate;

	@OneToMany(
			mappedBy = "career",
			cascade = CascadeType.ALL,
			orphanRemoval = true
	)
	@OrderBy("id ASC")
	@BatchSize(size = 100)
	private List<CareerKeyword> keywords = new ArrayList<>();

	@OneToMany(
			mappedBy = "career",
			cascade = CascadeType.ALL,
			orphanRemoval = true
	)
	@OrderBy("id ASC")
	@BatchSize(size = 100)
	private List<CareerContent> contents = new ArrayList<>();

	public static Career create(String title, String startDate, String endDate) {
		Career career = new Career();

		career.changeTitle(title);
		career.changePeriod(startDate, endDate);

		return career;
	}

	public void changeTitle(String title) {
		Check.notBlank(title, () -> new CustomException("career title is null"));

		this.title = title.strip();
	}

	public void changePeriod(String startDate, String endDate) {
		String start = blankToNull(startDate);
		String end = blankToNull(endDate);

		if (start == null) {
			throw new CustomException("career start date is null");
		}

		if (!YmdDate.isValid(start)) {
			throw new CustomException("career start date is invalid");
		}

		if (end != null && !YmdDate.isValid(end)) {
			throw new CustomException("career end date is invalid");
		}

		if (end != null && start.compareTo(end) > 0) {
			throw new CustomException("career end date is before start date");
		}

		this.startDate = start;
		this.endDate = end;
	}

	public void addKeyword(String name) {
		this.keywords.add(CareerKeyword.create(name, this));
	}

	public void clearKeywords() {
		this.keywords.clear();
	}

	public void addContent(String content) {
		this.contents.add(CareerContent.create(content, this));
	}

	public void clearContents() {
		this.contents.clear();
	}

	public List<CareerKeyword> getKeywords() {
		return Collections.unmodifiableList(this.keywords);
	}

	public List<CareerContent> getContents() {
		return Collections.unmodifiableList(this.contents);
	}

	private static String blankToNull(String value) {
		return (value == null || value.isBlank()) ? null : value.strip();
	}
}
