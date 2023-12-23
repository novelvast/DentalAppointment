package com.microservice.personalinfoservice.controller;

import com.microservice.common.api.CommonResult;
import com.microservice.common.domain.UserDto;
import com.microservice.personalinfoservice.dto.PatientDto;
import com.microservice.personalinfoservice.service.PatientInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * 患者信息管理Controller
 *
 * @author zhao
 * @date 2023/12/14
 */
@Api(tags = "PatientInfoController", description = "患者信息管理")
@RestController
@RequestMapping("/api/patient")
public class PatientInfoController {

    @Autowired
    private PatientInfoService patientInfoService;


    @ApiOperation("患者注册")
    @PostMapping("/register")
    public CommonResult register(@RequestParam String username,
                              @RequestParam String password,
                              @RequestParam String phone,
                              @RequestParam String email,
                              @RequestParam String idNumber,
                              @RequestParam String name,
                              @RequestParam String gender,
                              @RequestParam String birthday){

        Boolean result = patientInfoService.register(username, password, phone, email, idNumber, name, gender, birthday);
        if(result == Boolean.TRUE) {
            return CommonResult.success(null,"注册成功");
        }
        else {
            return CommonResult.failed("注册失败");
        }
    }

    @ApiOperation("患者登陆")
    @PostMapping("/login")
    public CommonResult login(@RequestParam String username,
                              @RequestParam String password){

        return patientInfoService.login(username, password);
    }

    // 根据患者名获取患者信息
    @ApiOperation("根据患者名获取患者信息")
    @GetMapping("/{patientName}")
    public CommonResult getByName(@PathVariable String patientName){
        PatientDto patientDto = patientInfoService.getByName(patientName);
        if(patientDto == null) {
            return CommonResult.failed("查无此人");
        }
        return CommonResult.success(patientDto);
    }

    @ApiOperation("修改患者信息")
    @PutMapping("/")
    public CommonResult updatePatientInfo(@RequestParam String username,
                                          @RequestParam String phone,
                                          @RequestParam String email,
                                          @RequestParam String IDNumber,
                                          @RequestParam String name,
                                          @RequestParam String gender,
                                          @RequestParam String birthday){
        Boolean result = patientInfoService.updateInfo(username, phone, email, IDNumber, name, gender, birthday);
        if(result == Boolean.TRUE) {
            return CommonResult.success(null,"修改成功");
        }
        else {
            return CommonResult.failed("修改失败");
        }

    }

    @ApiOperation("修改患者密码")
    @PutMapping("/password")
    public CommonResult updatePatientInfo(@RequestParam String username,
                                          @RequestParam String password){
        Boolean result = patientInfoService.updatePassword(username, password);
        if(result == Boolean.TRUE) {
            return CommonResult.success(null,"修改成功");
        }
        else {
            return CommonResult.failed("修改失败");
        }

    }


    @ApiOperation("根据患者名获取认证信息")
    @GetMapping("/loadByUsername")
    public UserDto loadUserByUsername(@RequestParam String username){
        return patientInfoService.loadUserByUsername(username);
    }

}
