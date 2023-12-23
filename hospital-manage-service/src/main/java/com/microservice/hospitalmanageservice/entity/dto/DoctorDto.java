package com.microservice.hospitalmanageservice.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDto {
    private String id;
    private String name;
    private Short gender;
    private LocalDate birth;
    private String phoneNumber;
    private String email;
    private String job;
    private Integer deptId;
}
