package com.microservice.personalinfoservice.controller;

import com.microservice.common.api.CommonResult;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/api/patient")
public class PatientInfoController {

    // TODO: 参数
    @PostMapping("/register")
    public CommonResult register(){

        return CommonResult.success(null);
    }

    @PostMapping("/login")
    public CommonResult login(@RequestParam String username, @RequestParam String password){

        return CommonResult.success(null);
    }

    @GetMapping("/{patientName}")
    public CommonResult getPatientInfo(@PathVariable String patientName){

        return CommonResult.success(null);
    }

    // TODO: 参数
    @PutMapping("/{patientName}")
    public CommonResult updatePatientInfo(@PathVariable String patientName){

        return CommonResult.success(null);
    }


}
