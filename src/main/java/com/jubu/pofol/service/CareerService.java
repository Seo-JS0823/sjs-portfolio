package com.jubu.pofol.service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jubu.pofol.domain.model.Career;
import com.jubu.pofol.domain.request.CareerRequests.CareerCreateRequest;
import com.jubu.pofol.domain.request.CareerRequests.CareerUpdateRequest;
import com.jubu.pofol.domain.response.CareerResponses.CareerListResponse;
import com.jubu.pofol.domain.response.CareerResponses.CareerResponse;
import com.jubu.pofol.repository.CareerRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CareerService {

	private final CareerRepo careerRepo;

	@Transactional(readOnly = true)
	public CareerListResponse getList() {
		List<Career> careers = careerRepo.findAll(
				Sort.by(Sort.Order.desc("startDate"), Sort.Order.desc("id"))
		);

		return CareerListResponse.of(careers);
	}

	@Transactional(readOnly = true)
	public CareerResponse getDetail(Long careerId) {
		Career career = careerRepo.findByIdOrThrow(careerId);

		return CareerResponse.of(career);
	}

	@Transactional
	public CareerResponse create(CareerCreateRequest request) {
		Career career = Career.create(
				request.title(),
				request.startDate(),
				request.endDate()
		);

		replaceKeywords(career, request.keywords());
		replaceContents(career, request.contents());

		careerRepo.save(career);

		return CareerResponse.of(career);
	}

	@Transactional
	public CareerResponse update(Long careerId, CareerUpdateRequest request) {
		Career career = careerRepo.findByIdOrThrow(careerId);

		if (!request.hasTitle()) {
			career.changeTitle(request.title());
		}

		if (!request.hasStartDate() || !request.hasEndDate()) {
			career.changePeriod(
					request.hasStartDate() ? career.getStartDate() : request.startDate(),
					request.hasEndDate() ? career.getEndDate() : request.endDate()
			);
		}

		if (!request.hasKeywords()) {
			replaceKeywords(career, request.keywords());
		}

		if (!request.hasContents()) {
			replaceContents(career, request.contents());
		}

		return CareerResponse.of(career);
	}

	@Transactional
	public void delete(Long careerId) {
		Career career = careerRepo.findByIdOrThrow(careerId);

		careerRepo.delete(career);
	}

	private void replaceKeywords(Career career, List<String> keywords) {
		career.clearKeywords();

		if (keywords == null) {
			return;
		}

		keywords.stream()
				.filter(Objects::nonNull)
				.map(String::strip)
				.filter(keyword -> !keyword.isEmpty())
				.collect(Collectors.toCollection(LinkedHashSet::new))
				.forEach(career::addKeyword);
	}

	private void replaceContents(Career career, List<String> contents) {
		career.clearContents();

		if (contents == null) {
			return;
		}

		contents.stream()
				.filter(Objects::nonNull)
				.map(String::strip)
				.filter(content -> !content.isEmpty())
				.forEach(career::addContent);
	}
}
