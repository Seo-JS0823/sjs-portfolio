package com.jubu.pofol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CustomRepo<T, ID> extends JpaRepository<T, ID> {

	T findByIdThrow(ID id);
}
