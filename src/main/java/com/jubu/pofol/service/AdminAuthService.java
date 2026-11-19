package com.jubu.pofol.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;

import org.springframework.stereotype.Service;

import com.jubu.pofol.config.AdminProperties;
import com.jubu.pofol.domain.CustomException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminAuthService {

	private static final String SESSION_KEY = "ADMIN_AUTHENTICATED";
	private static final Duration CODE_TTL = Duration.ofMinutes(5);
	private static final Duration RESEND_COOLDOWN = Duration.ofSeconds(60);
	private static final int MAX_ATTEMPTS = 5;

	private final MailSenderService mailSender;
	private final AdminProperties adminProperties;
	private final SecureRandom random = new SecureRandom();

	private String code;
	private Instant expiresAt;
	private Instant lastSentAt;
	private int attempts;

	public synchronized void sendCode() {
		String email = adminProperties.email();

		if (email == null || email.isBlank()) {
			throw new CustomException("관리자 이메일이 설정되지 않았습니다.");
		}

		Instant now = Instant.now();

		if (lastSentAt != null && now.isBefore(lastSentAt.plus(RESEND_COOLDOWN))) {
			throw new CustomException("잠시 후 다시 요청해 주세요.");
		}

		String newCode = "%06d".formatted(random.nextInt(1_000_000));

		mailSender.send(
				email,
				"[pofol] 관리자 인증 코드",
				"인증 코드: " + newCode + "\n\n" + CODE_TTL.toMinutes() + "분 동안 유효합니다."
		);

		code = newCode;
		expiresAt = now.plus(CODE_TTL);
		lastSentAt = now;
		attempts = 0;
	}

	public synchronized boolean verify(String input, HttpServletRequest request) {
		if (code == null || Instant.now().isAfter(expiresAt)) {
			clearCode();
			throw new CustomException("인증 코드가 없거나 만료되었습니다. 다시 받아 주세요.");
		}

		attempts++;

		String value = input == null ? "" : input.strip();
		boolean matched = MessageDigest.isEqual(
				code.getBytes(StandardCharsets.UTF_8),
				value.getBytes(StandardCharsets.UTF_8)
		);

		if (matched) {
			clearCode();
			request.getSession();
			request.changeSessionId();
			request.getSession().setAttribute(SESSION_KEY, Boolean.TRUE);
			return true;
		}

		if (attempts >= MAX_ATTEMPTS) {
			clearCode();
			throw new CustomException("입력 횟수를 초과했습니다. 코드를 다시 받아 주세요.");
		}

		return false;
	}

	public boolean isAuthenticated(HttpServletRequest request) {
		HttpSession session = request.getSession(false);

		return session != null && Boolean.TRUE.equals(session.getAttribute(SESSION_KEY));
	}

	public void logout(HttpServletRequest request) {
		HttpSession session = request.getSession(false);

		if (session != null) {
			session.invalidate();
		}
	}

	private void clearCode() {
		code = null;
		expiresAt = null;
		attempts = 0;
	}
}
