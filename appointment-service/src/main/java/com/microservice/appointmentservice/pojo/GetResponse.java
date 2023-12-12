package com.microservice.appointmentservice.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetResponse {
    private String patientId;

    private String doctorId;

    private LocalDateTime orderTime;

    private LocalDateTime clinicTime;

    private String orderDepartment;

    private String diseaseDescription;
}