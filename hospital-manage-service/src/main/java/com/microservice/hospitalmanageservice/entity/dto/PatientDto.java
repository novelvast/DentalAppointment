package com.microservice.hospitalmanageservice.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDto {
    private Integer id;
    private String username;
    private String phone;
    private String email;
    private Integer idNumber;
    private String name;
    private String gender;
    private String birthday;
}
