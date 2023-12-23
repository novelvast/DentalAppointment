package com.microservice.messageservice.service.impl;

import com.microservice.common.api.CommonResult;
import org.springframework.stereotype.Service;

@Service
public interface Message_service {
    CommonResult return_message(String username);


    String send_message(String userbody);
}
