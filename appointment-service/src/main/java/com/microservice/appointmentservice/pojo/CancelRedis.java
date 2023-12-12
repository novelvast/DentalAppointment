package com.microservice.appointmentservice.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelRedis {
    private String patientId;
    private String date;
    private Integer count;
}
