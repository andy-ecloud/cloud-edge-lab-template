package com.webcomm.cloudedge.api.frontend.webflux.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.http.HttpClient;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Value("${backend-endpoint}")
    public String backendEndpoint;

    @Autowired
    public WebClient webClient;

    @GetMapping
    public Mono<?> test(@RequestParam(required = false, defaultValue = "0") int delay) {
//        logger.info("Test API is invoked. (Non-Blocking)");

        return webClient.get().uri(backendEndpoint + "?delay=" + delay).retrieve().bodyToMono(String.class)
                .onErrorResume(throwable -> {
                    return Mono.error(throwable);
                });
    }
}
