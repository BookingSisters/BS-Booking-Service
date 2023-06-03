package com.bs.booking.controllers;

import com.bs.booking.models.Test;
import com.bs.booking.services.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/test")
public class TestController {

    private final TestService testService;

    @GetMapping("/hello")
    public String helloWorld() {
        return testService.helloWorld();
    }

    @GetMapping
    public ResponseEntity<List<Test>> getTest() {
        List<Test> testList = testService.getTests();
        return new ResponseEntity<>(testList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Test> createTest(@RequestBody Test createTest) {
        Test created = testService.createTest(createTest);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Test> updateTest(@PathVariable long id, @RequestBody Test updateTest) {
        updateTest.setTestId(id);
        Test updated = testService.updateTest(updateTest);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public HttpStatus updateTest(@PathVariable long id) {
        testService.deleteTest(id);
        return HttpStatus.OK;
    }
}
