package com.microservice.approvalservice.service.impl;

import com.microservice.approvalservice.service.FeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeignServiceImpl{
    @Autowired
    FeignService feign;

    public String send(String userbody){
        return feign.send_message(userbody);
    }
}
