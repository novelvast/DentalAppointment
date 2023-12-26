package com.microservice.appointmentservice.service;

import com.microservice.common.api.CommonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="hospital-manage-service",path="/api/hospital")
public interface HospitalManageFeignService {
    @GetMapping("/{hospitalId}/administrator")
    CommonResult getHospitalAdministrator(@PathVariable String hospitalId);
}
