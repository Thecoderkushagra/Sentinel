package com.TheCoderKushagra.Sentinel;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SentinelApplication {

	public static void main(String[] args) {
		SpringApplication.run(SentinelApplication.class, args);
	}

	@Bean
	public ChatClient Gemini(GoogleGenAiChatModel model) {
		return ChatClient.builder(model)
				.build();
	}

}
