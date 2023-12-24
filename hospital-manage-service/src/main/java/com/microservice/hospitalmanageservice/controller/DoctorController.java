package com.microservice.hospitalmanageservice.controller;

import com.microservice.common.api.CommonResult;
import com.microservice.hospitalmanageservice.entity.vo.DoctorVo;
import com.microservice.hospitalmanageservice.service.IDoctorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("{hospitalId}/doctor/")
public class DoctorController {
    @Resource
    private IDoctorService doctorService;

    @GetMapping("all")
    public CommonResult getAll(@PathVariable String hospitalId){
        List<DoctorVo> doctorVo = doctorService.getAll(hospitalId);
        return CommonResult.success(doctorVo);
    }
    // 查医生信息
    @GetMapping("/id/{doctorId}")
    public CommonResult getDoctor(@PathVariable Integer hospitalId, @PathVariable String doctorId) {
        // TODO: 去对应医院调医生信息
        DoctorVo doctorVo = doctorService.getById(hospitalId, doctorId);
        return CommonResult.success(doctorVo);
    }
}
