package com.microservice.personalinfoservice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.microservice.common.api.CommonResult;
import com.microservice.common.domain.UserDto;
import com.microservice.personalinfoservice.dto.DoctorDto;
import com.microservice.personalinfoservice.entity.DoctorInfo;
import com.microservice.personalinfoservice.entity.PatientInfo;
import com.microservice.personalinfoservice.mapper.DoctorInfoMapper;
import com.microservice.personalinfoservice.service.AuthService;
import com.microservice.personalinfoservice.service.DoctorInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DoctorInfoServiceImpl implements DoctorInfoService {

    @Autowired
    private DoctorInfoMapper doctorInfoMapper;

    @Autowired
    private AuthService authService;

    @Override
    public Boolean register(String username, String password, String phone, String email, String hospital,
                            String name, Integer jobNumber) {
        // 查询是否已有该用户
        if (getByName(username) == null){
            return Boolean.FALSE;
        }

        // TODO 调用hospital-manage判断医生工号

        // 没有对该用户进行添加操作
        int result = doctorInfoMapper.insert(new DoctorInfo(username, password, phone, email, hospital,
                name, jobNumber));
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
        params.put("client_id", "client_patient");
        params.put("client_secret","patient");
        params.put("grant_type","password");
        params.put("username",username);
        params.put("password",password);
        return CommonResult.success(authService.getAccessToken(params));
    }

    @Override
    public DoctorDto getByName(String username) {
        DoctorInfo doctorInfo = getAllInfoByName(username);

        if(doctorInfo != null) {
            DoctorDto doctorDto = new DoctorDto();
            BeanUtil.copyProperties(doctorInfo, doctorDto);
            return doctorDto;
        }
        return null;
    }

    @Override
    public DoctorInfo getAllInfoByName(String username) {
        QueryWrapper<DoctorInfo> doctorInfoQueryWrapper = new QueryWrapper<>();
        doctorInfoQueryWrapper.eq("username", username);

        return doctorInfoMapper.selectOne(doctorInfoQueryWrapper);
    }

    @Override
    public Boolean updateInfo(String username, String phone, String email, String hospital, String name,
                              Integer jobNumber) {
        UpdateWrapper<DoctorInfo> doctorInfoUpdateWrapper = new UpdateWrapper<>();
        doctorInfoUpdateWrapper.eq("username",username)
                .set("phone", phone)
                .set("email", email)
                .set("hospital", hospital)
                .set("name", name)
                .set("jobNumber", jobNumber);

        int result = doctorInfoMapper.update(null, doctorInfoUpdateWrapper);

        if(result == 1){
            return Boolean.TRUE;
        }
        else {
            return Boolean.FALSE;
        }
    }

    @Override
    public Boolean updatePassword(String username, String password) {
        UpdateWrapper<DoctorInfo> doctorInfoUpdateWrapper = new UpdateWrapper<>();
        doctorInfoUpdateWrapper.eq("username",username)
                .set("password", password);

        int result = doctorInfoMapper.update(null, doctorInfoUpdateWrapper);

        if(result == 1){
            return Boolean.TRUE;
        }
        else {
            return Boolean.FALSE;
        }
    }

    @Override
    public UserDto loadUserByUsername(String username) {
        DoctorInfo doctorInfo = getAllInfoByName(username);

        if(doctorInfo != null) {
            UserDto userDto = new UserDto();
            BeanUtil.copyProperties(doctorInfo, userDto);
            return userDto;
        }
        return null;
    }
}
