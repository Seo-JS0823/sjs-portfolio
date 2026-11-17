package com.jubu.pofol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jubu.pofol.domain.model.Tag;

@Repository
public interface TagRepo extends JpaRepository<Tag, Long> {

}
