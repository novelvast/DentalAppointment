package com.microservice.hospitalmanageservice.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDto {
    private String patientId;
    private String doctorId;
    private Integer deptId;
    private LocalDateTime appointmentDateTime;
    private Short patientGender;
    private LocalDate patientBirth;
    private String patientName;
    private String patientCondition;
}
