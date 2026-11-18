package com.jubu.pofol.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.jubu.pofol.repository.CustomRepoImpl;

@Configuration
@EnableJpaRepositories(
		basePackages = "com.jubu.pofol.repository",
		repositoryBaseClass = CustomRepoImpl.class
)
public class JpaConfig {

}
