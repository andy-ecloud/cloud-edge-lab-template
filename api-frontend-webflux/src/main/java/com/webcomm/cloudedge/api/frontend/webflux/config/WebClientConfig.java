package com.webcomm.cloudedge.api.frontend.webflux.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient webClient() {
        ConnectionProvider connectionProvider = ConnectionProvider.builder("http").maxConnections(1000)
                .pendingAcquireMaxCount(1000)
                .pendingAcquireTimeout(Duration.ofMillis(ConnectionProvider.DEFAULT_POOL_ACQUIRE_TIMEOUT))
                .maxIdleTime(Duration.ZERO).build();

        HttpClient httpClient = HttpClient.create(connectionProvider).keepAlive(false);

        WebClient.Builder builder = WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient));

        return builder.build();
    }
}
