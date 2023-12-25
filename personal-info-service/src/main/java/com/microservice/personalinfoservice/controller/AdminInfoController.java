package com.microservice.personalinfoservice.controller;

import com.microservice.common.api.CommonResult;
import com.microservice.common.domain.UserDto;
import com.microservice.personalinfoservice.dto.AdminDto;
import com.microservice.personalinfoservice.dto.DoctorDto;
import com.microservice.personalinfoservice.service.AdminInfoService;
import com.microservice.personalinfoservice.service.DoctorInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员信息管理Controller
 *
 * @author zhao
 * @date 2023/12/14
 */
@Api(tags = "AdminInfoController", description = "管理员信息管理")
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/admin")
public class AdminInfoController {
    @Autowired
    private AdminInfoService adminInfoService;

    @ApiOperation("管理员注册")
    @PostMapping("/register")
    public CommonResult register(@RequestParam String username,
                                 @RequestParam String password,
                                 @RequestParam String phone,
                                 @RequestParam String email,
                                 @RequestParam Integer hospitalId,
                                 @RequestParam String name,
                                 @RequestParam Integer jobNumber){

        Boolean result = adminInfoService.register(username, password, phone, email, hospitalId, name, jobNumber);
        if(result == Boolean.TRUE) {
            return CommonResult.success(null,"注册成功");
        }
        else {
            return CommonResult.failed("注册失败");
        }
    }

    @ApiOperation("管理员登陆")
    @PostMapping("/login")
    public CommonResult login(@RequestParam String username,
                              @RequestParam String password){

        return adminInfoService.login(username, password);
    }

    // 根据患者名获取患者信息
    @ApiOperation("根据管理员名获取管理员信息")
    @GetMapping("/{patientName}")
    public CommonResult getByName(@PathVariable String patientName){
        AdminDto adminDto = adminInfoService.getByName(patientName);
        if(adminDto == null) {
            return CommonResult.failed("查无此人");
        }
        return CommonResult.success(adminDto);
    }

    @ApiOperation("修改管理员信息")
    @PutMapping ("")
    public CommonResult updatePatientInfo(@RequestParam String username,
                                          @RequestParam String phone,
                                          @RequestParam String email,
                                          @RequestParam Integer hospitalId,
                                          @RequestParam String name,
                                          @RequestParam Integer jobNumber){
        Boolean result = adminInfoService.updateInfo(username, phone, email, hospitalId, name, jobNumber);
        if(result == Boolean.TRUE) {
            return CommonResult.success(null,"修改成功");
        }
        else {
            return CommonResult.failed("修改失败");
        }

    }

    @ApiOperation("修改管理员密码")
    @PutMapping("/password")
    public CommonResult updatePatientInfo(@RequestParam String username,
                                          @RequestParam String password){
        Boolean result = adminInfoService.updatePassword(username, password);
        if(result == Boolean.TRUE) {
            return CommonResult.success(null,"修改成功");
        }
        else {
            return CommonResult.failed("修改失败");
        }

    }


    @ApiOperation("根据管理员名获取认证信息")
    @GetMapping("/loadByUsername")
    public UserDto loadUserByUsername(@RequestParam String username){
        return adminInfoService.loadUserByUsername(username);
    }
}
