package com.jubu.pofol.repository;

import org.springframework.stereotype.Repository;

import com.jubu.pofol.domain.model.Post;

@Repository
public interface PostRepo extends CustomRepo<Post, Long> {

}
