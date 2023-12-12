package com.microservice.approvalservice.service.iml;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name="message-service",path="/api/message")
public interface messageuse {
    @GetMapping("/show")
    String XZW_test();

    @PostMapping("/send")
    String send_message(@RequestBody String username);
}
