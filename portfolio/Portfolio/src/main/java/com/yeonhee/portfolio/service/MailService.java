package com.yeonhee.portfolio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
	@Autowired
	private JavaMailSender mailSender;
	
	public void sendMail(String subject, String message) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom("mouse97_@naver.com");// 보내는 사람
		simpleMailMessage.setTo("mouse97_@naver.com");	// 받는 사람
		simpleMailMessage.setSubject(subject);			// 제목	
		simpleMailMessage.setText(message); 			// 내용 
		
		mailSender.send(simpleMailMessage);
	}
	
}
