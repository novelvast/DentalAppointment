package com.microservice.hospitalmanageservice.controller;

import com.microservice.common.api.CommonResult;
import com.microservice.hospitalmanageservice.service.IDoctorService;
import com.microservice.hospitalmanageservice.entity.vo.DoctorVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("{hospitalId}")
public class DoctorController {
    @Resource
    private IDoctorService doctorService;

    // 查医生信息
    @GetMapping("doctor/{doctorId}")
    public CommonResult getDoctor(@PathVariable Integer hospitalId, @PathVariable String doctorId) {
        // TODO: 去对应医院调医生信息
        DoctorVo doctorVo = doctorService.getById(hospitalId, doctorId);
        return CommonResult.success(doctorVo);
    }
}
