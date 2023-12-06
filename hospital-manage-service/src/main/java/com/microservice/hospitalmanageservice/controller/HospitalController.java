package com.microservice.hospitalmanageservice.controller;

import com.microservice.common.api.CommonResult;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/hospital/")
public class HospitalController {

    // 查医院信息
    @GetMapping("/{hospitalId}")
    public CommonResult getHospitalById(@RequestParam Long id){
        // TODO: 从自己数据库取医院信息
        return CommonResult.success(null);
    }

    // 向医院写预约信息
    @PostMapping("/appointment")
    public CommonResult appointment(@RequestParam String hospital_name,
                                    @RequestParam String doctor_name,
                                    @RequestParam String patient_name,
                                    @DateTimeFormat(pattern = "yyyy-MM-dd")
                                        @RequestParam LocalDate order_time,
                                    @DateTimeFormat(pattern = "yyyy-MM-dd")
                                        @RequestParam LocalDate clinic_time,
                                    @RequestParam String order_department,
                                    @RequestParam String disease_description){
        // TODO: 写到对应医院数据库
        return CommonResult.success(null);
    }

    //向医院删预约信息

    //查病历
}
