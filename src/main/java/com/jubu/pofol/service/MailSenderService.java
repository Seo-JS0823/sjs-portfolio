package com.jubu.pofol.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailSenderService {

	private final JavaMailSender mailSender;
}
