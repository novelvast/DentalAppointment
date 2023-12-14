package com.microservice.personalinfoservice.service;

import com.microservice.common.api.CommonResult;
import com.microservice.common.domain.UserDto;
import com.microservice.personalinfoservice.dto.AdminDto;
import com.microservice.personalinfoservice.dto.DoctorDto;
import com.microservice.personalinfoservice.entity.AdminInfo;
import com.microservice.personalinfoservice.entity.DoctorInfo;

/**
 * 管理员信息管理Service
 *
 * @author zhao
 * @date 2023/12/14
 */
public interface AdminInfoService {

    /**
     * 管理员注册
     */
    Boolean register(String username, String password, String phone, String email, String hospital, String name,
                     Integer jobNumber);

    /**
     * 管理员登陆
     */
    CommonResult login(String username, String password);

    /**
     * 根据管理员名获取患者信息
     */
    AdminDto getByName(String username);

    /**
     * 根据管理员名获取管理员所有信息
     */
    AdminInfo getAllInfoByName(String username);

    /**
     * 修改管理员信息
     */
    Boolean updateInfo(String username, String phone, String email, String hospital, String name, Integer jobNumber);

    /**
     * 修改管理员密码
     */
    Boolean updatePassword(String username, String password);

    /**
     * 获取用户名和密码，认证服务调用
     */
    UserDto loadUserByUsername(String username);
}
