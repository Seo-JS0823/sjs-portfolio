package com.jubu.pofol.repository;

import org.springframework.stereotype.Repository;

import com.jubu.pofol.domain.model.PostContent;

@Repository
public interface PostContentRepo extends CustomRepo<PostContent, Long> {

}
