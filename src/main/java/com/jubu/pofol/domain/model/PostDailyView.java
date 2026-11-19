package com.jubu.pofol.domain.model;

import java.time.LocalDate;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.jubu.pofol.domain.CustomException;
import com.jubu.pofol.util.Check;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
		name = "post_daily_views",
		uniqueConstraints = @UniqueConstraint(
				name = "uk_post_daily_views_post_date",
				columnNames = {"post_id", "report_date"}
		),
		indexes = @Index(name = "idx_post_daily_views_date", columnList = "report_date")
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PostDailyView {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Post post;

	@Column(name = "report_date", nullable = false)
	private LocalDate reportDate;

	@Column(name = "view_count", nullable = false)
	private long viewCount;

	public static PostDailyView create(Post post, LocalDate reportDate) {
		Check.notNull(post, () -> new CustomException("post is null"));
		Check.notNull(reportDate, () -> new CustomException("report date is null"));

		PostDailyView view = new PostDailyView();

		view.post = post;
		view.reportDate = reportDate;
		view.viewCount = 0;

		return view;
	}

	public void increase() {
		this.viewCount++;
	}
}
