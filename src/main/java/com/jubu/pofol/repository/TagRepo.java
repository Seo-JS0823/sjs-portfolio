package com.jubu.pofol.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jubu.pofol.domain.model.Tag;
import com.jubu.pofol.domain.model.TagGroup;

@Repository
public interface TagRepo extends CustomRepo<Tag, Long> {

	@Query(
			"""
			SELECT t.id
			FROM Tag t
			WHERE t.tagGroup = :tagGroup
			"""
	)
	List<Long> findByTagGroup(@Param("tagGroup") TagGroup tagGroup);
}
