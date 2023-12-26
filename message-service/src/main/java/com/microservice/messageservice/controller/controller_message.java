package com.microservice.messageservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.common.api.CommonResult;
import com.microservice.messageservice.entity.Message;
import com.microservice.messageservice.list_to_json;
import com.microservice.messageservice.mapper.MessageMapper;
import com.microservice.messageservice.service.impl.Message_service;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/message")
public class controller_message {
    @Autowired
    Message_service message_service;

    @ApiOperation("返回某用户信息列表")
    @PostMapping("/get_usermessage")
    public CommonResult return_message(@RequestParam String username){
        return message_service.return_message(username);
    }
    @ApiOperation("网站内发送信息，并发送邮件给该用户")
    @PostMapping("/send")
    public String send_message(@RequestBody String userbody){
        return message_service.send_message(userbody);
    }

}
