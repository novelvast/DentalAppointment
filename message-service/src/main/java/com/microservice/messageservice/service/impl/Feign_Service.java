package com.microservice.messageservice.service.impl;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="personal-info-service",path="/api")
public interface Feign_Service {

    @PostMapping("/send1")
    String get_email_doctor(@RequestParam String username);

    @PostMapping("/send2")
    String get_email_patient(@RequestParam String username);
}
