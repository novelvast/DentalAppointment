package com.microservice.personalinfoservice.service;

import com.microservice.common.api.CommonResult;
import com.microservice.common.domain.UserDto;
import com.microservice.personalinfoservice.dto.PatientDto;
import com.microservice.personalinfoservice.entity.PatientInfo;

import java.time.LocalDate;

/**
 * 患者信息管理Service
 *
 * @author zhao
 * @date 2023/12/14
 */
public interface PatientInfoService {

    /**
     * 患者注册
     */
    Boolean register(String username, String password, String phone, String email, String idNumber, String name,
                     String gender, LocalDate birthday);

    /**
     * 患者登陆
     */
    CommonResult login(String username, String password);

    /**
     * 根据患者名获取患者信息
     */
    PatientDto getByName(String username);

    /**
     * 根据患者id获取患者信息
     */
    PatientDto getById(Integer patientId);

    /**
     * 根据患者名获取患者所有信息
     */
    PatientInfo getAllInfoByName(String username);

    /**
     * 修改患者信息
     */
    Boolean updateInfo(String username, String phone, String email, String IDNumber, String name,
                       String gender, LocalDate birthday);

    /**
     * 修改患者密码
     */
    Boolean updatePassword(String username, String password);

    /**
     * 获取用户名和密码，认证服务调用
     */
    UserDto loadUserByUsername(String username);

    /**
     * 获取邮箱，消息服务调用
     */
    String getEmailByName(String username);


}
