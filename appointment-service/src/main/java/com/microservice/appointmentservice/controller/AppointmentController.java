package com.microservice.appointmentservice.controller;

import com.microservice.appointmentservice.dto.AppointmentDto;
import com.microservice.appointmentservice.pojo.BookRequest;
import com.microservice.appointmentservice.pojo.BookResponse;
import com.microservice.appointmentservice.service.AppointmentService;
import com.microservice.common.api.CommonResult;
import com.sun.xml.internal.bind.v2.TODO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    @ApiOperation("根据编号查询已有预约")
    @GetMapping("/get/{orderId}")
    @CrossOrigin(origins = "*")
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
    @CrossOrigin(origins = "*")
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
    @CrossOrigin(origins = "*")
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
    @CrossOrigin(origins = "*")
    //时间格式形如2023-12-11 10:43:07
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
    @CrossOrigin(origins = "*")
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
    @CrossOrigin(origins = "*")
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
