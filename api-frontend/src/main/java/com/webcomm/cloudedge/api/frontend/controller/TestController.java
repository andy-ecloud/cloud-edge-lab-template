package com.webcomm.cloudedge.api.frontend.controller;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Value("${backend-endpoint}")
    private String backendEndpoint;

    @Autowired
    public CloseableHttpClient httpClient;

    @GetMapping
    public ResponseEntity<?> test(@RequestParam(required = false, defaultValue = "0") int delay) {
//        logger.info("Test API is invoked. (Blocking)");

        HttpGet httpGet = new HttpGet(backendEndpoint + "?delay=" + delay);

        String content;

        try (CloseableHttpResponse httpResponse = httpClient.execute(httpGet)) {
            content = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
            EntityUtils.consume(httpResponse.getEntity());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        } finally {
            if (httpGet != null) {
                httpGet.releaseConnection();
                httpGet = null;
            }
        }

        return ResponseEntity.ok(content);
    }

}
