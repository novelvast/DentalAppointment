package com.microservice.personalinfoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class PersonalInfoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonalInfoServiceApplication.class, args);
    }

}
