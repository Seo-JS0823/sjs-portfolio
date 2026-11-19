package com.jubu.pofol.config;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.jubu.pofol.service.AdminAuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminAuthInterceptor implements HandlerInterceptor {

	private final AdminAuthService adminAuthService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (adminAuthService.isAuthenticated(request)) {
			return true;
		}

		String uri = request.getRequestURI();

		if (uri.startsWith(request.getContextPath() + "/api/")) {
			if (HttpMethod.GET.matches(request.getMethod())) {
				return true;
			}

			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
			response.getWriter().write("{\"message\":\"인증이 필요합니다.\"}");
			return false;
		}

		response.sendRedirect(request.getContextPath() + "/po/admin/login");
		return false;
	}
}
