package com.microservice.appointmentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManageAddDto {
    private String hospital;
    private String patientId;
    private String doctorId;
    private LocalDateTime orderTime;
    private LocalDateTime clinicTime;
    private String diseaseDescription;
}
