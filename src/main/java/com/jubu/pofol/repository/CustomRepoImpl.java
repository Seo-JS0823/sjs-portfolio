package com.jubu.pofol.repository;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.jubu.pofol.domain.CustomException;

import jakarta.persistence.EntityManager;

public class CustomRepoImpl<T, ID> extends SimpleJpaRepository<T, ID> implements CustomRepo<T, ID> {

	private final String entityName;
	
	public CustomRepoImpl(
			JpaEntityInformation<T, ?> entityInformation,
			EntityManager entityManager
	) {
		super(entityInformation, entityManager);
		this.entityName = entityInformation.getJavaType().getSimpleName();
	}

	@Override
	public T findByIdOrThrow(ID id) {
		return findById(id)
				.orElseThrow(() -> new CustomException(entityName + " not found"));
	}
}
