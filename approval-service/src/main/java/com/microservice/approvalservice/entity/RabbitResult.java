package com.microservice.approvalservice.entity;

import lombok.Data;

@Data
public class RabbitResult {

    private String data;
    private String type;

    public void setData(String data) {
        this.data = data;
    }

    public void setType(String type) {
        this.type = type;
    }
}
