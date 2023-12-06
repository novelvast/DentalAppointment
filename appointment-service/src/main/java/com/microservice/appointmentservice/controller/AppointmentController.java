package com.microservice.appointmentservice.controller;

import com.microservice.common.api.CommonResult;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    // 查询已有预约
    @GetMapping("/{appointmentId}")
    public CommonResult getAppointmentById(){
        // TODO: 从自己数据库中调
        return CommonResult.success(null);
    }

    // 预约
    @PostMapping("/create")
    public CommonResult create(@RequestParam String doctor_name,
                               @RequestParam String patient_name,
                                   @DateTimeFormat(pattern = "yyyy-MM-dd")
                               @RequestParam LocalDate order_time,
                                   @DateTimeFormat(pattern = "yyyy-MM-dd")
                               @RequestParam LocalDate clinic_time,
                               @RequestParam String order_department,
                               @RequestParam String disease_description){
        // 是不是需要医院参数？？？
        // TODO: 向数据库中添加
        // TODO: 调用hospital manage，添加到对应医院
        return CommonResult.success(null);
    }

    // 用户取消预约
    @PutMapping("/cancel/patient/{appointmentId}")
    public CommonResult patientCancel(){
        // TODO: 修改数据库
        // TODO: 调用hospital manage，修改对应医院数据库
        // TODO: 三次以上，调审核、消息微服务，管理员审核
        return CommonResult.success(null);
    }

    // 医生取消预约
    @PutMapping("/cancel/doctor/{appointmentId}")
    public CommonResult doctorCancel(){
        // TODO: 修改数据库
        // TODO: 调用hospital manage，修改对应医院数据库
        // TODO: 三次以上，调审核、消息微服务，管理员审核
        return CommonResult.success(null);
    }


}
