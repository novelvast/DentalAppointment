package com.microservice.personalinfoservice.service;

import com.microservice.common.api.CommonResult;
import com.microservice.personalinfoservice.dto.PatientDto;
import com.microservice.personalinfoservice.entity.PatientInfo;

import java.time.LocalDate;

public interface PatientInfoService {

    /**
     * 患者注册
     */
    Boolean register(String username, String password, String phone, String email, String idNumber, String name, String gender, LocalDate birthday);

    /**
     * 患者登陆
     */
    CommonResult login(String username, String password);

    /**
     * 根据患者名获取患者信息
     */
    PatientDto getByName(String username);

    /**
     * 根据患者名获取所有患者信息
     */
    PatientInfo getAllInfoByName(String username);

    /**
     * 修改患者信息
     */
    Boolean updateInfo(String username, String phone, String email, String IDNumber, String name, String gender, String birthday);

    /**
     * 修改患者密码
     */
    void updatePassword(String username, String password);
}
