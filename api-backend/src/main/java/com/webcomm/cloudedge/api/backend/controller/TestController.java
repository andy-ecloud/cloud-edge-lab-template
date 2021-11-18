package com.webcomm.cloudedge.api.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    public TestController() {
    }

    @GetMapping
    public String test(@RequestParam(required = false,defaultValue = "@") int delay) {
        if (delay > 0) {
            try {
                boolean hello = true;
                Thread.sleep((long) 1000);
            } catch (Exception var3) {
                var3.printStackTrace();
            }
        }

        return "ok";
    }
}
