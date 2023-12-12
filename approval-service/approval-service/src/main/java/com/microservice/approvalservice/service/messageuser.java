package com.microservice.approvalservice.service;

import com.microservice.approvalservice.service.iml.messageuse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class messageuser {
    @Autowired
    messageuse use;
    public String user(Integer approvalId){
        String username;
        if(approvalId!=0){
            username="徐焯文";
        }
        else{
            username="123";
        }
        return use.send_message(username);
    }

}
