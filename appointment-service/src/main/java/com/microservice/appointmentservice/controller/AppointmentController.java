package com.microservice.appointmentservice.controller;

import com.microservice.appointmentservice.dto.AppointmentDto;
import com.microservice.appointmentservice.pojo.BookRequest;
import com.microservice.appointmentservice.service.AppointmentService;
import com.microservice.common.api.CommonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    @ApiOperation("根据编号查询已有预约")
    @GetMapping("/get/{orderId}")
    public CommonResult getAppointmentById(@PathVariable Integer orderId){
        // TODO: 从自己数据库中调
        AppointmentDto result =  appointmentService.getAppointmentById(orderId);
        if(result!=null)
            return CommonResult.success(result);
        else
            return CommonResult.failed("no results");
    }
    // 患者查询已有预约
    @ApiOperation("患者查询已有预约")
    @GetMapping("/get/patient/{patientId}")
    public CommonResult getAppointmentByPatientId(@PathVariable String patientId){
        // TODO: 从自己数据库中调
        List<AppointmentDto> result =  appointmentService.getAppointmentByPatientId(patientId);
        if(result!=null)
            return CommonResult.success(result);
        else
            return CommonResult.failed("no results");
    }
    // 医生查询已有预约
    @ApiOperation("医生查询已有预约")
    @GetMapping("/get/doctor/{doctorId}")
    public CommonResult getAppointmentByDoctorId(@PathVariable String doctorId){
        // TODO: 从自己数据库中调
        List<AppointmentDto> result =  appointmentService.getAppointmentByDoctorId(doctorId);
        if(result!=null)
            return CommonResult.success(result);
        else
            return CommonResult.failed("no results");
    }
    // 预约
    @ApiOperation("患者预约")
    @PostMapping("/book")
    public CommonResult bookAppointment(@RequestBody BookRequest bookRequest) {
        int result = appointmentService.book(bookRequest);
        if(result==-1){
            return CommonResult.failed("failed to book an appointment");
        } else if (result==0) {
            return CommonResult.failed("waiting for qpproval");
        } else{
            return CommonResult.success(result,"an appointment booked successfully");
        }
    }

    // 用户取消预约
    @ApiOperation("用户取消预约")
    @PutMapping("/cancel/patient/{appointmentId}")
    public CommonResult patientCancel(@PathVariable Integer appointmentId){
        // TODO: 修改数据库
        // TODO: 调用hospital manage，修改对应医院数据库
        // TODO: 三次以上，调审核、消息微服务，管理员审核
        if(appointmentService.patientCancel(appointmentId)){
            return CommonResult.success(null,"User canceled an appointment successfully");
        }else{
            return CommonResult.failed("User failed to cancel an appointment successfully");
        }
    }

    // 医生取消预约
    @ApiOperation("医生取消预约")
    @PutMapping("/cancel/doctor/{appointmentId}")
    public CommonResult doctorCancel(@PathVariable Integer appointmentId){
        // TODO: 修改数据库
        // TODO: 调用hospital manage，修改对应医院数据库
        // TODO: 三次以上，调审核、消息微服务，管理员审核
        if(appointmentService.doctorCancel(appointmentId)){
            return CommonResult.success(null,"Doctor canceled an appointment successfully");
        }else{
            return CommonResult.failed("Doctor failed to cancel an appointment successfully");
        }
    }
}
