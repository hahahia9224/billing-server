package com.linecorp.linepay.controller;

import com.linecorp.linepay.config.LineApiResponse;
import com.linecorp.linepay.exception.AmountNotEnoughException;
import com.linecorp.linepay.exception.InvalidParameterException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @GetMapping("/ok")
    public ResponseEntity<LineApiResponse> ok() {
        return ResponseEntity.ok(LineApiResponse.ok(1L, 991900));
    }

    @GetMapping("/test")
    public String sample() {
        throw new InvalidParameterException("title is required");
    }


    @GetMapping("/test1")
    public String sample1() {
        throw new InvalidParameterException("title is required2");
    }

    @GetMapping("/test2")
    public String sample2() {
        throw new AmountNotEnoughException();
    }

}
