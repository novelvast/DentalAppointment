package com.microservice.approvalservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="appointment-service",path="/api/appointment")
public interface AppointmentFeignService {
    @PostMapping("/?")
    String send_orderid(@RequestParam Integer orderid);
}
