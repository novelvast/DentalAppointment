package com.microservice.hospitalmanageservice.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDto1{
    private Integer doctorId;
    private String patientId;
    private String dept;
    private String patientName;
    private Integer patientGender;
    private Integer patientAge;
    private LocalDateTime treatmentTime;
    private String conditionDescription;
}
