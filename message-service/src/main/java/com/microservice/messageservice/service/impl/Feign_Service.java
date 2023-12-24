package com.microservice.messageservice.service.impl;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="personal-info-service")
public interface Feign_Service {

    @PostMapping("api/doctor/email")
    String get_email_doctor(@RequestParam String username);

    @PostMapping("api/patient/email")
    String get_email_patient(@RequestParam String username);
}
