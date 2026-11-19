package com.jubu.pofol.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.jubu.pofol.domain.model.Post;
import com.jubu.pofol.domain.model.PostDailyView;

@Repository
public interface PostDailyViewRepo extends CustomRepo<PostDailyView, Long> {

	Optional<PostDailyView> findByPostAndReportDate(Post post, LocalDate reportDate);

	List<PostDailyView> findByReportDate(LocalDate reportDate);
}
