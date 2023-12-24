package com.microservice.hospitalmanageservice.client;

import com.microservice.common.api.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "personal-info-service")
public interface PersonInfoClient {
    @GetMapping("/api/doctor/id/{doctorId}")
    CommonResult getDoctorById(@PathVariable Integer doctorId);

    @GetMapping("/api/patient/id/{patientId}")
    CommonResult getPatientById(@PathVariable Integer patientId);
}
