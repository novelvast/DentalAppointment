package com.microservice.personalinfoservice.controller;


import com.microservice.common.api.CommonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/test")
    public CommonResult<String> test() {
        return CommonResult.success("hello world");
    }
}
