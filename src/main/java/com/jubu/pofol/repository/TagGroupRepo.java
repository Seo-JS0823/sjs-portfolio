package com.jubu.pofol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jubu.pofol.domain.model.TagGroup;

@Repository
public interface TagGroupRepo extends JpaRepository<TagGroup, Long> {

}
