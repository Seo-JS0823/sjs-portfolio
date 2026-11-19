package com.jubu.pofol.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * application.yaml 의 aboutMe 설정값
 */
@ConfigurationProperties(prefix = "about-me")
public record AboutMeProperties(String name, String phone, String email) {

}
