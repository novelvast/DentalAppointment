package com.microservice.hospitalmanageservice.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentVo {
    private String appointmentId;
    private Integer doctorId;
    private String patientId;
    private Integer deptId;
    private LocalDateTime appointmentDateTime;
    private Short patientGender;
    private LocalDate patientBirth;
    private String patientName;
    private String patientCondition;
}
