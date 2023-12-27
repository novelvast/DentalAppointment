package com.microservice.personalinfoservice.controller;

import com.microservice.common.api.CommonResult;
import com.microservice.common.domain.UserDto;
import com.microservice.personalinfoservice.dto.DoctorDto;
import com.microservice.personalinfoservice.dto.PatientDto;
import com.microservice.personalinfoservice.service.DoctorInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 医生信息管理Controller
 *
 * @author zhao
 * @date 2023/12/14
 */
@Api(tags = "DoctorInfoController", description = "医生信息管理")
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/doctor")
public class DoctorInfoController {

    @Autowired
    private DoctorInfoService doctorInfoService;

    @ApiOperation("医生注册")
    @PostMapping("/register")
    public CommonResult register(@RequestParam String username,
                                 @RequestParam String password,
                                 @RequestParam String phone,
                                 @RequestParam String email,
                                 @RequestParam Integer hospitalId,
                                 @RequestParam String name,
                                 @RequestParam Integer jobNumber,
                                 @RequestParam String department,
                                 @RequestParam String photoUrl){

        Boolean result = doctorInfoService.register(username, password, phone, email, hospitalId, name, jobNumber, department, photoUrl);
        if(result == Boolean.TRUE) {
            return CommonResult.success(null,"注册成功");
        }
        else {
            return CommonResult.failed("注册失败");
        }
    }

    @ApiOperation("医生登陆")
    @PostMapping("/login")
    public CommonResult login(@RequestParam String username,
                              @RequestParam String password){

        return doctorInfoService.login(username, password);
    }

    // 根据患者名获取患者信息
    @ApiOperation("根据医生名获取医生信息")
    @GetMapping("/{patientName}")
    public CommonResult getByName(@PathVariable String patientName){
        DoctorDto doctorDto = doctorInfoService.getByName(patientName);
        if(doctorDto == null) {
            return CommonResult.failed("查无此人");
        }
        return CommonResult.success(doctorDto);
    }

    @ApiOperation("修改医生信息")
    @PutMapping("")
    public CommonResult updatePatientInfo(@RequestParam String username,
                                          @RequestParam String phone,
                                          @RequestParam String email,
                                          @RequestParam Integer hospitalId,
                                          @RequestParam String name,
                                          @RequestParam Integer jobNumber,
                                          @RequestParam String department,
                                          @RequestParam String photoUrl){
        Boolean result = doctorInfoService.updateInfo(username, phone, email, hospitalId, name, jobNumber, department, photoUrl);
        if(result == Boolean.TRUE) {
            return CommonResult.success(null,"修改成功");
        }
        else {
            return CommonResult.failed("修改失败");
        }

    }

    @ApiOperation("修改医生密码")
    @PutMapping("/password")
    public CommonResult updatePatientInfo(@RequestParam String username,
                                          @RequestParam String password){
        Boolean result = doctorInfoService.updatePassword(username, password);
        if(result == Boolean.TRUE) {
            return CommonResult.success(null,"修改成功");
        }
        else {
            return CommonResult.failed("修改失败");
        }

    }

    @ApiOperation("根据医生id获取医生信息")
    @GetMapping("/id/{doctorId}")
    public CommonResult getById(@PathVariable Integer doctorId){
        DoctorDto doctorDto = doctorInfoService.getById(doctorId);
        if(doctorDto == null) {
            return CommonResult.failed("查无此人");
        }
        return CommonResult.success(doctorDto);
    }

    @ApiOperation("根据医生名获取认证信息")
    @GetMapping("/loadByUsername")
    public UserDto loadUserByUsername(@RequestParam String username){
        return doctorInfoService.loadUserByUsername(username);
    }

    @ApiOperation("根据医生名获取邮箱")
    @PostMapping("/email")
    public String getEmailByName(@RequestParam String username){
        return doctorInfoService.getEmailByName(username);
    }

    @ApiOperation("获取医院所有医生")
    @GetMapping("/getByHospital/{hospitalId}")
    public CommonResult getByHospital(@PathVariable Integer hospitalId) {
        List<DoctorDto> doctorDtoList = doctorInfoService.getByHospital(hospitalId);

        if(doctorDtoList.isEmpty()) {
            return CommonResult.failed("该医院没有医生数据");
        }
        else {
            return CommonResult.success(doctorDtoList);
        }

    }
}
