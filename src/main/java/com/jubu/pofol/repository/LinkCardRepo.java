package com.jubu.pofol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jubu.pofol.domain.model.LinkCard;

@Repository
public interface LinkCardRepo extends JpaRepository<LinkCard, Long> {

}
