package com.microservice.appointmentservice.controller;

import com.microservice.appointmentservice.dto.AppointmentDto;
import com.microservice.appointmentservice.pojo.BookRequest;
import com.microservice.appointmentservice.pojo.CancelRequest;
import com.microservice.appointmentservice.service.AppointmentService;
import com.microservice.common.api.CommonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/appointment")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    @ApiOperation("根据编号查询已有预约")
    @GetMapping("/get/{orderId}")
    @CrossOrigin(origins = "*")
    public CommonResult getAppointmentById(@PathVariable Integer orderId){
        AppointmentDto result =  appointmentService.getAppointmentById(orderId);
        if(result!=null)
            return CommonResult.success(result);
        else
            return CommonResult.failed("no results");
    }
    // 患者查询已有预约
    @ApiOperation("患者查询已有预约")
    @GetMapping("/get/patient/{patientId}")
    @CrossOrigin(origins = "*")
    public CommonResult getAppointmentByPatientId(@PathVariable String patientId){
        List<AppointmentDto> result =  appointmentService.getAppointmentByPatientId(patientId);
        if(result!=null)
            return CommonResult.success(result);
        else
            return CommonResult.failed("no results");
    }
    // 医生查询已有预约
    @ApiOperation("医生查询已有预约")
    @GetMapping("/get/doctor/{doctorId}")
    @CrossOrigin(origins = "*")
    public CommonResult getAppointmentByDoctorId(@PathVariable String doctorId){
        List<AppointmentDto> result =  appointmentService.getAppointmentByDoctorId(doctorId);
        if(result!=null)
            return CommonResult.success(result);
        else
            return CommonResult.failed("no results");
    }
    // 预约
    @ApiOperation("患者预约")
    @PostMapping("/book")
    @CrossOrigin(origins = "*")
    //时间格式形如2023-12-11T10:43:07
    public CommonResult bookAppointment(@RequestBody BookRequest bookRequest) {
        int result = appointmentService.book(bookRequest);
        if(result==-1){
            return CommonResult.failed("failed to book an appointment");
        } else if (result==0) {
            return CommonResult.failed("waiting for approval");
        } else{
            return CommonResult.success(result,"an appointment booked successfully");
        }
    }

    // 用户取消预约
    @ApiOperation("用户取消预约")
    @PutMapping("/cancel/patient")
    @CrossOrigin(origins = "*")
    public CommonResult patientCancel(@RequestBody CancelRequest cancelRequest){
        int result =appointmentService.patientCancel(cancelRequest);
        if(result==-1){
            return CommonResult.failed("failed to cancel an appointment");
        } else if (result==0) {
            return CommonResult.failed("waiting for approval");
        } else{
            return CommonResult.success(result,"an appointment canceled successfully");
        }
    }

    // 医生取消预约
    @ApiOperation("医生取消预约")
    @PutMapping("/cancel/doctor")
    @CrossOrigin(origins = "*")
    public CommonResult doctorCancel(@RequestBody CancelRequest cancelRequest){
        if(appointmentService.doctorCancel(cancelRequest)){
            return CommonResult.success(null,"Doctor canceled an appointment successfully");
        }else{
            return CommonResult.failed("Doctor failed to cancel an appointment successfully");
        }
    }
}
