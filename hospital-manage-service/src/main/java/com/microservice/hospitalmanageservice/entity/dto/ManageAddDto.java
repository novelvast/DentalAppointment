package com.microservice.hospitalmanageservice.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManageAddDto {
    private String hospital;
    private String patientId;
    private String doctorId;
    private LocalDateTime clinicTime;
    private String diseaseDescription;
}
