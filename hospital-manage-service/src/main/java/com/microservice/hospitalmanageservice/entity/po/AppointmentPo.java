package com.microservice.hospitalmanageservice.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentPo {
    private Integer patientId;
    private Integer doctorId;
    private LocalDateTime appointmentDateTime;
    private Short patientGender;
    private LocalDate patientBirth;
    private String patientName;
    private String patientCondition;
    private Short status;
}
