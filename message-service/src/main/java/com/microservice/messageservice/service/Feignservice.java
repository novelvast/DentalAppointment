package com.microservice.messageservice.service;
import com.microservice.messageservice.service.impl.Feign_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Feignservice{
    @Autowired
    Feign_Service feign;

    public String get_doctor(String username){
        return feign.get_email_doctor(username);
    }

    public String get_patient(String username){
        return feign.get_email_patient(username);
    }
}