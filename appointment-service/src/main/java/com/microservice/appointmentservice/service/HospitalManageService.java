package com.microservice.appointmentservice.service;

import com.microservice.common.api.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@FeignClient(value = "hospital-manage-service")
public interface HospitalManageService {

    @GetMapping("/api/hospital/{hospitalId}")
    CommonResult getHospitalById(@RequestParam Long id);

    @PostMapping("/api/hospital/appointment")
    CommonResult appointment(@RequestParam String hospital_name,
                             @RequestParam String doctor_name,
                             @RequestParam String patient_name,
                             @DateTimeFormat(pattern = "yyyy-MM-dd")
                             @RequestParam LocalDate order_time,
                             @DateTimeFormat(pattern = "yyyy-MM-dd")
                             @RequestParam LocalDate clinic_time,
                             @RequestParam String order_department,
                             @RequestParam String disease_description);

}