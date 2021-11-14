package com.webcomm.cloudedge.api.frontend.webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FrontendWebfluxApplication {

	public static void main(String[] args) {
		System.out.println(
				"Spring Application Initializing... [" + FrontendWebfluxApplication.class.getCanonicalName() + "]");

		SpringApplication.run(FrontendWebfluxApplication.class, args);

		System.out.println(
				"Spring Application Initialized. [" + FrontendWebfluxApplication.class.getCanonicalName() + "]");
	}

}
