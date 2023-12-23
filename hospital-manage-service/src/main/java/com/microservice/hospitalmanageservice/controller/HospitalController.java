package com.microservice.hospitalmanageservice.controller;

import com.microservice.common.api.CommonResult;
import com.microservice.hospitalmanageservice.service.IHospitalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping
public class HospitalController {
    @Resource
    private IHospitalService hospitalService;

    @GetMapping
    public CommonResult getAll(){
        return CommonResult.success(hospitalService.list());
    }
    // 查医院信息
    @GetMapping("/{hospitalId}")
    public CommonResult getHospitalById(@PathVariable Integer hospitalId) {
        // TODO: 从自己数据库取医院信息
        log.info("{}", hospitalId);
        return CommonResult.success(hospitalService.getById(hospitalId));
    }
}
