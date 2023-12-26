package com.microservice.messageservice.controller;

import com.microservice.messageservice.entity.Email;
import com.microservice.messageservice.service.impl.SendEmail;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/message")
@Api(value = "发送邮件接口",tags = {"发送邮件接口"})
public class controller_sendmail {
    @Autowired
    private SendEmail sendMailService;

    @PostMapping("/simple")
    public void SendSimpleMessage(@RequestBody Email mailRequest) {
        sendMailService.sendSimpleMail(mailRequest);
    }

    @PostMapping("/html")
    public void SendHtmlMessage(@RequestBody Email mailRequest) { sendMailService.sendHtmlMail(mailRequest);}
}
