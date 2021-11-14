package com.webcomm.cloudedge.api.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		System.out.println("Spring Application Initializing... [" + BackendApplication.class.getCanonicalName() + "]");

		SpringApplication.run(BackendApplication.class, args);

		System.out.println("Spring Application Initialized. [" + BackendApplication.class.getCanonicalName() + "]");
	}

}
