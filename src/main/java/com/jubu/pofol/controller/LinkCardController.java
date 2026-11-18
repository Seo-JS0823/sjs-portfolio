package com.jubu.pofol.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jubu.pofol.repository.LinkCardRepo;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/linkcard")
@RequiredArgsConstructor
public class LinkCardController {

	private final LinkCardRepo lcRepo;
}
