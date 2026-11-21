package com.jubu.pofol.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jubu.pofol.domain.request.CareerRequests.CareerCreateRequest;
import com.jubu.pofol.domain.request.CareerRequests.CareerUpdateRequest;
import com.jubu.pofol.domain.response.CareerResponses.CareerListResponse;
import com.jubu.pofol.domain.response.CareerResponses.CareerResponse;
import com.jubu.pofol.service.CareerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/careers")
public class CareerController {

	private final CareerService careerService;

	@PostMapping
	public ResponseEntity<CareerResponse> create(
			@RequestBody CareerCreateRequest request
	) {
		CareerResponse response = careerService.create(request);

		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<CareerListResponse> getList() {
		CareerListResponse response = careerService.getList();

		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CareerResponse> getOne(
			@PathVariable("id") Long careerId
	) {
		CareerResponse response = careerService.getDetail(careerId);

		return ResponseEntity.ok(response);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<CareerResponse> update(
			@RequestBody CareerUpdateRequest request,
			@PathVariable("id") Long careerId
	) {
		CareerResponse response = careerService.update(careerId, request);

		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(
			@PathVariable("id") Long careerId
	) {
		careerService.delete(careerId);

		return ResponseEntity.noContent().build();
	}
}
