package com.jsp.emailwriter.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsp.emailwriter.app.EmailRequest;

@Service
public class EmailGeneratorService {
	
	private final WebClient webClient;
	
	@Value("${gemini.api.url}")
	private String geminiApiUrl;
	
	@Value("${gemini.api.key}")
	private String geminiApiKey;
	
	public EmailGeneratorService(WebClient.Builder webClient) {
		this.webClient = webClient.build();
	}
	
	public String generateEmailReply(EmailRequest emailRequest) {
		
		String prompt = builtPrompt(emailRequest);
		
		// crafting a request
		 Map<String, Object> requestBody = Map.of(
				 "contents", new Object[] {
						 Map.of("parts", new Object[] {
								 Map.of("text", prompt)
						 })
				 }
		);
		 
		// do request and get res
		 String res = webClient.post()
				 .uri(geminiApiUrl + geminiApiKey)
				 .header("Content-Type", "application/json")
				 .bodyValue(requestBody)
				 .retrieve()
				 .bodyToMono(String.class)
				 .block();
		 
		 // res
		 return extractResContent(res);
		 
	}
	
	
	
	

	private String extractResContent(String res) {
		try {
			// conert java object to json data
			ObjectMapper mapper = new ObjectMapper();
			// use json node
			JsonNode node = mapper.readTree(res);
			return node.path("candidates")
					.get(0)
					.path("content")
					.path("parts")
					.get(0)
					.path("text")
					.asText();
		}
		catch(Exception e) {
			return "Error "+ e.getMessage();
		}
	}

	private String builtPrompt(EmailRequest emailRequest) {
		StringBuilder prompt = new StringBuilder();
		prompt.append("Generate a professional email reply for hte following email content. Please don't generate a subject line ");
		if(emailRequest.getTone() != null && !emailRequest.getTone().isEmpty()) {
			prompt.append("Use a ").append(emailRequest.getTone()).append(" tone.");
		}
		prompt.append("\nOriginal email: \n").append(emailRequest.getEmailContent());
		return prompt.toString();
	}

}
