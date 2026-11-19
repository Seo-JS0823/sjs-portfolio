package com.jubu.pofol.controller.page;

import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jubu.pofol.domain.CustomException;
import com.jubu.pofol.service.AdminAuthService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/po/admin")
@RequiredArgsConstructor
public class AdminPageController {

	private static final String REDIRECT_LOGIN = "redirect:/po/admin/login";
	private static final String REDIRECT_ADMIN = "redirect:/po/admin";

	private final AdminAuthService adminAuthService;

	@GetMapping
	public String admin() {
		return "admin/index";
	}

	@GetMapping("/login")
	public String login(HttpServletRequest request) {
		if (adminAuthService.isAuthenticated(request)) {
			return REDIRECT_ADMIN;
		}

		return "admin/login";
	}

	@PostMapping("/login/code")
	public String sendCode(RedirectAttributes attributes) {
		try {
			adminAuthService.sendCode();
			attributes.addFlashAttribute("sent", true);
		} catch (CustomException e) {
			attributes.addFlashAttribute("error", e.getMessage());
		} catch (MailException e) {
			log.error("관리자 인증 메일 전송 실패", e);
			attributes.addFlashAttribute("error", "메일 전송에 실패했습니다. 메일 설정을 확인해 주세요.");
		}

		return REDIRECT_LOGIN;
	}

	@PostMapping("/login/verify")
	public String verify(
			@RequestParam("code") String code,
			HttpServletRequest request,
			RedirectAttributes attributes
	) {
		try {
			if (adminAuthService.verify(code, request)) {
				return REDIRECT_ADMIN;
			}

			attributes.addFlashAttribute("error", "인증 코드가 일치하지 않습니다.");
			attributes.addFlashAttribute("sent", true);
		} catch (CustomException e) {
			attributes.addFlashAttribute("error", e.getMessage());
		}

		return REDIRECT_LOGIN;
	}

	@PostMapping("/logout")
	public String logout(HttpServletRequest request) {
		adminAuthService.logout(request);

		return "redirect:/";
	}
}
