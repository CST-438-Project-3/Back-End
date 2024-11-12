package com.cst438.project3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Project3Application {

	public static void main(String[] args) {

		int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
		SpringApplication app = new SpringApplication(Project3Application.class);
		app.setDefaultProperties(java.util.Collections.singletonMap("server.port", port));
		//app.run(args);
	}
}
