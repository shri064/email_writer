package com.jsp.emailwriter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jsp.emailwriter.app.EmailRequest;
import com.jsp.emailwriter.service.EmailGeneratorService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/email")
@CrossOrigin(origins = "*")
public class EmailGeneratorController {
	
	private final EmailGeneratorService emailGeneratorService;
	
	public EmailGeneratorController(EmailGeneratorService emailGeneratorService) {
        this.emailGeneratorService = emailGeneratorService;
    }
	
	@PostMapping("/generate")
	public ResponseEntity<String> generateEmail(@RequestBody EmailRequest emailRequest){
		String res = emailGeneratorService.generateEmailReply(emailRequest);
		return ResponseEntity.ok(res);
	}
}
