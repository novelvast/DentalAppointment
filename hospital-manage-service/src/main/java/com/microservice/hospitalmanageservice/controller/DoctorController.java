package com.microservice.hospitalmanageservice.controller;

import com.microservice.common.api.CommonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hospital/doctor")
public class DoctorController {

    // 查医生信息
    @GetMapping("/{hospitalId}/{doctorId}")
    public CommonResult getDoctor(){
        // TODO: 去对应医院调医生信息
        return CommonResult.success(null);
    }

    // 查KPI
    @GetMapping("kpi/{hospitalId}/{doctorId}")
    public CommonResult getDoctorKpi(){
        // TODO: 去对应医院调医生信息
        return CommonResult.success(null);
    }
}
