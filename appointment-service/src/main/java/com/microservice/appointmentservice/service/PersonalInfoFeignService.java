package com.microservice.appointmentservice.service;

import com.microservice.common.api.CommonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="personal-info-service",path="/api")
public interface PersonalInfoFeignService {

    @ApiOperation("根据患者名获取患者信息")
    @GetMapping("/patient/{patientName}")
    CommonResult getByName(@PathVariable String patientName);
}
