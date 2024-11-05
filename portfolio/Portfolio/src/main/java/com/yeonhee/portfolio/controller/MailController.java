package com.yeonhee.portfolio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yeonhee.portfolio.service.MailService;

@Controller
public class MailController {
	
	@Autowired
	private MailService mailService;
	
	@PostMapping("/sendMail")
	public String sendMail(
			@RequestParam String name,
			@RequestParam String phone,
			@RequestParam String message) {
		
		String subject = "[" + name + "에서 메일이 도착했습니다]";
		String body = "기업명 : " + name + "\n연락처 : " + phone + "\n\n" + message;
		
		mailService.sendMail(subject, body);
		return "redirect:/index";
	}
	
}
