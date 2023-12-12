package com.microservice.appointmentservice.service;

import com.microservice.common.api.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@FeignClient(value = "hospital-manage-service")
public interface HospitalManageServiceFeignClient {

    @GetMapping("/api/hospital/{hospitalId}")
    CommonResult getHospitalById(@RequestParam Long id);

    @PostMapping("/api/hospital/appointment")
    CommonResult appointment(@RequestParam String hospital_name,
                             @RequestParam String doctor_name,
                             @RequestParam String patient_name,
                             @RequestParam LocalDateTime order_time,
                             @RequestParam LocalDateTime clinic_time,
                             @RequestParam String order_department,
                             @RequestParam String disease_description);

}
