package com.jubu.pofol.repository;

import org.springframework.stereotype.Repository;

import com.jubu.pofol.domain.model.Career;

@Repository
public interface CareerRepo extends CustomRepo<Career, Long> {

}
