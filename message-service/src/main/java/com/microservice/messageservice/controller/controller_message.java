package com.microservice.messageservice.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/message")
public class controller_message {

    @GetMapping("/show")
    public String XZW_test(){
        return"{\"information\":123456789}";
    }

    @PostMapping("/send")
    public String send_message(@RequestBody String username){
        return"已发送"+username;
    }
}
