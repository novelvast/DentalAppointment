package com.microservice.personalinfoservice.controller;

import com.microservice.common.api.CommonResult;
import com.microservice.personalinfoservice.dto.PatientDto;
import com.microservice.personalinfoservice.service.PatientInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/api/patient")
public class PatientInfoController {

    @Autowired
    private PatientInfoService patientInfoService;


    // 患者注册
    @PostMapping("/register")
    public CommonResult register(@RequestParam String username,
                              @RequestParam String password,
                              @RequestParam String phone,
                              @RequestParam String email,
                              @RequestParam String IDNumber,
                              @RequestParam String name,
                              @RequestParam String gender,
                              @RequestParam String birthday){

        patientInfoService.register(username, password, phone, email, IDNumber, name, gender, birthday);
        return CommonResult.success(null,"注册成功");
    }

    // 患者登陆
    @PostMapping("/login")
    public CommonResult login(@RequestParam String username,
                              @RequestParam String password){

        return patientInfoService.login(username, password);
    }

    // 根据患者名获取患者信息
    @GetMapping("/{patientName}")
    public PatientDto getPatientByName(@PathVariable String patientName){

        return patientInfoService.getPatientByName(patientName);
    }

    // 修改患者信息
    @PutMapping("/update")
    public CommonResult updatePatientInfo(@RequestParam String username,
                                          @RequestParam String phone,
                                          @RequestParam String email,
                                          @RequestParam String IDNumber,
                                          @RequestParam String name,
                                          @RequestParam String gender,
                                          @RequestParam String birthday){
        patientInfoService.updateInfo(username, phone, email, IDNumber, name, gender, birthday);
        return CommonResult.success(null,"修改成功");

    }

    // 修改密码
    @PutMapping("/update/password")
    public CommonResult updatePatientInfo(@RequestParam String username,
                                          @RequestParam String password){
        patientInfoService.updatePassword(username, password);
        return CommonResult.success(null,"修改成功");

    }


}
