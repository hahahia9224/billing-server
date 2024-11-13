package com.pay.api.controller;

import com.pay.api.exception.AmountNotEnoughExceptionApi;
import com.pay.api.exception.InvalidParameterExceptionApi;
import com.pay.api.model.PayApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @GetMapping("/ok")
    public ResponseEntity<PayApiResponse> ok() {
        return ResponseEntity.ok(PayApiResponse.ok(1L, 991900));
    }

    @GetMapping("/test")
    public String sample() {
        throw new InvalidParameterExceptionApi("title is required");
    }


    @GetMapping("/test1")
    public String sample1() {
        throw new InvalidParameterExceptionApi("title is required2");
    }

    @GetMapping("/test2")
    public String sample2() {
        throw new AmountNotEnoughExceptionApi();
    }

}
