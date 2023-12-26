package com.microservice.approvalservice.service;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="message-service",path="/api/message")
public interface FeignService {
    @PostMapping("/send")
    String send_message(@RequestBody String userbody);
}
