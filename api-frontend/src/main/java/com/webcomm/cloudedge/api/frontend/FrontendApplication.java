package com.webcomm.cloudedge.api.frontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FrontendApplication {

	public static void main(String[] args) {
		System.out.println("Spring Application Initializing... [" + FrontendApplication.class.getCanonicalName() + "]");

		SpringApplication.run(FrontendApplication.class, args);

		System.out.println("Spring Application Initialized. [" + FrontendApplication.class.getCanonicalName() + "]");
	}

}
