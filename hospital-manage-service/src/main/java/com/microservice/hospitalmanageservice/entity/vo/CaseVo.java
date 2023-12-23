package com.microservice.hospitalmanageservice.entity.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CaseVo {
    private String caseId;
    private String patientId;
    private String doctorId;
    private Integer deptId;
    private LocalDateTime createTime;
    private Short patientGender;
    private LocalDate patientBirth;
    private String patientName;
    private String checkResult;
    private String treatment;
}
