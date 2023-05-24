package com.bs.ps.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TestController {

    private final TestService testService;

    @GetMapping("/test")
    public String helloWorld() {
        return testService.helloWorld();
    }
}
