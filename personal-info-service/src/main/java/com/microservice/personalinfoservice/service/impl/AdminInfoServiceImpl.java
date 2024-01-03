package com.microservice.personalinfoservice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.microservice.common.api.CommonResult;
import com.microservice.common.domain.UserDto;
import com.microservice.personalinfoservice.dto.AdminDto;
import com.microservice.personalinfoservice.dto.PatientDto;
import com.microservice.personalinfoservice.entity.AdminInfo;
import com.microservice.personalinfoservice.entity.DoctorInfo;
import com.microservice.personalinfoservice.entity.PatientInfo;
import com.microservice.personalinfoservice.mapper.AdminInfoMapper;
import com.microservice.personalinfoservice.service.AdminInfoService;
import com.microservice.personalinfoservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AdminInfoServiceImpl implements AdminInfoService {

    @Autowired
    private AdminInfoMapper adminInfoMapper;

    @Autowired
    private AuthService authService;

    @Override
    public Boolean register(String username, String password, String phone, String email, Integer hospitalId,
                            String name, Integer jobNumber) {
        // 查询是否已有该用户
        if (getByName(username) != null){
            return Boolean.FALSE;
        }

        // 没有对该用户进行添加操作
        AdminInfo adminInfo = new AdminInfo();
        adminInfo.setUsername(username);
        adminInfo.setPassword(password);
        adminInfo.setPhone(phone);
        adminInfo.setEmail(email);
        adminInfo.setHospitalId(hospitalId);
        adminInfo.setName(name);
        adminInfo.setJobNumber(jobNumber);
        int result = adminInfoMapper.insert(adminInfo);
        if(result == 1){
            return Boolean.TRUE;
        }
        else {
            return Boolean.FALSE;
        }
    }

    @Override
    public CommonResult login(String username, String password) {
        if(StrUtil.isEmpty(username)||StrUtil.isEmpty(password)){
            return CommonResult.failed("用户名或密码不能为空！");
        }
        Map<String, String> params = new HashMap<>();
        params.put("client_id", "client_admin");
        params.put("client_secret","admin");
        params.put("grant_type","password");
        params.put("username",username);
        params.put("password",password);
        return CommonResult.success(authService.getAccessToken(params));
    }

    @Override
    public AdminDto getByName(String username) {
        AdminInfo adminInfo = getAllInfoByName(username);

        if(adminInfo != null) {
            AdminDto adminDto = new AdminDto();
            BeanUtil.copyProperties(adminInfo, adminDto);
            return adminDto;
        }
        return null;
    }

    @Override
    public AdminDto getById(Integer adminId) {
        AdminInfo adminInfo = adminInfoMapper.selectById(adminId);

        if(adminInfo != null) {
            AdminDto adminDto = new AdminDto();
            BeanUtil.copyProperties(adminInfo, adminDto);
            return adminDto;
        }
        return null;
    }

    @Override
    public AdminInfo getAllInfoByName(String username) {
        QueryWrapper<AdminInfo> adminInfoQueryWrapper = new QueryWrapper<>();
        adminInfoQueryWrapper.eq("username", username);

        return adminInfoMapper.selectOne(adminInfoQueryWrapper);
    }

    @Override
    public Boolean updateInfo(String username, String phone, String email, Integer hospitalId, String name,
                              Integer jobNumber) {
        UpdateWrapper<AdminInfo> adminInfoUpdateWrapper = new UpdateWrapper<>();
        adminInfoUpdateWrapper.eq("username",username)
                .set("phone", phone)
                .set("email", email)
                .set("hospital_id", hospitalId)
                .set("name", name)
                .set("job_number", jobNumber);

        int result = adminInfoMapper.update(null, adminInfoUpdateWrapper);

        if(result == 1){
            return Boolean.TRUE;
        }
        else {
            return Boolean.FALSE;
        }
    }

    @Override
    public Boolean updatePassword(String username, String password) {
        UpdateWrapper<AdminInfo> adminInfoUpdateWrapper = new UpdateWrapper<>();
        adminInfoUpdateWrapper.eq("username",username)
                .set("password", password);

        int result = adminInfoMapper.update(null, adminInfoUpdateWrapper);

        if(result == 1){
            return Boolean.TRUE;
        }
        else {
            return Boolean.FALSE;
        }
    }

    @Override
    public UserDto loadUserByUsername(String username) {
        AdminInfo adminInfo = getAllInfoByName(username);

        if(adminInfo != null) {
            UserDto userDto = new UserDto();
            BeanUtil.copyProperties(adminInfo, userDto);
            return userDto;
        }
        return null;
    }
}
