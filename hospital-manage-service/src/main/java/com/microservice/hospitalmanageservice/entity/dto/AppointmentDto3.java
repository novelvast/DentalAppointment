package com.microservice.hospitalmanageservice.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDto3{
    private String appointmentId;
    private String patientId;
    private String doctorId;
    private Integer deptId;
    private String patientName;
    private Integer patientGender;
    private String patientBirth;
    private LocalDateTime appointmentDateTime;
    private String patientCondition;
}
