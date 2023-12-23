package com.microservice.personalinfoservice.service;

import com.microservice.common.api.CommonResult;
import com.microservice.common.domain.UserDto;
import com.microservice.personalinfoservice.dto.DoctorDto;
import com.microservice.personalinfoservice.dto.PatientDto;
import com.microservice.personalinfoservice.entity.DoctorInfo;
import com.microservice.personalinfoservice.entity.PatientInfo;

/**
 * 医生信息管理Service
 *
 * @author zhao
 * @date 2023/12/14
 */
public interface DoctorInfoService {

    /**
     * 医生注册
     */
    Boolean register(String username, String password, String phone, String email, Integer hospitalId, String name,
                     Integer jobNumber);

    /**
     * 医生登陆
     */
    CommonResult login(String username, String password);

    /**
     * 根据医生名获取医生信息
     */
    DoctorDto getByName(String username);

    /**
     * 根据医生id获取医生信息
     */
    DoctorDto getById(Integer doctorId);

    /**
     * 根据医生名获取医生所有信息
     */
    DoctorInfo getAllInfoByName(String username);

    /**
     * 修改医生信息
     */
    Boolean updateInfo(String username, String phone, String email, Integer hospitalId, String name, Integer jobNumber);

    /**
     * 修改医生密码
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

