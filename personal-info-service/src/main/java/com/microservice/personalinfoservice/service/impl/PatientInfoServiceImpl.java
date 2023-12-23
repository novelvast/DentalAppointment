package com.microservice.personalinfoservice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.microservice.common.api.CommonResult;
import com.microservice.common.domain.UserDto;
import com.microservice.personalinfoservice.dto.PatientDto;
import com.microservice.personalinfoservice.entity.PatientInfo;
import com.microservice.personalinfoservice.mapper.PatientInfoMapper;
import com.microservice.personalinfoservice.service.AuthService;
import com.microservice.personalinfoservice.service.PatientInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class PatientInfoServiceImpl implements PatientInfoService {

    @Autowired
    private PatientInfoMapper patientInfoMapper;

    @Autowired
    private AuthService authService;

    @Override
    public Boolean register(String username, String password, String phone, String email, String idNumber,
                            String name, String gender, String birthday) {

        // 查询是否已有该用户
        if (getByName(username) != null){
            return Boolean.FALSE;
        }

        // 没有对该用户进行添加操作
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setUsername(username);
        patientInfo.setPassword(password);
        patientInfo.setPhone(phone);
        patientInfo.setEmail(email);
        patientInfo.setIdNumber(idNumber);
        patientInfo.setName(name);
        patientInfo.setGender(gender);
        patientInfo.setBirthday(birthday);


        int result = patientInfoMapper.insert(patientInfo);

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
    public PatientDto getByName(String username) {
        PatientInfo patientInfo = getAllInfoByName(username);

        if(patientInfo != null) {
            PatientDto patientDto = new PatientDto();
            BeanUtil.copyProperties(patientInfo, patientDto);
            return patientDto;
        }
        return null;
    }

    @Override
    public PatientDto getById(Integer patientId) {
        PatientInfo patientInfo = patientInfoMapper.selectById(patientId);

        if(patientInfo != null) {
            PatientDto patientDto = new PatientDto();
            BeanUtil.copyProperties(patientInfo, patientDto);
            return patientDto;
        }
        return null;
    }

    @Override
    public PatientInfo getAllInfoByName(String username) {
        QueryWrapper<PatientInfo> patientInfoQueryWrapper = new QueryWrapper<>();
        patientInfoQueryWrapper.eq("username", username);

        return patientInfoMapper.selectOne(patientInfoQueryWrapper);
    }

    @Override
    public Boolean updateInfo(String username, String phone, String email, String IDNumber, String name,
                              String gender, String birthday) {
        UpdateWrapper<PatientInfo> patientInfoUpdateWrapper = new UpdateWrapper<>();
        patientInfoUpdateWrapper.eq("username",username)
                .set("phone", phone)
                .set("email", email)
                .set("id_number", IDNumber)
                .set("name", name)
                .set("gender", gender)
                .set("birthday", birthday);

        int result = patientInfoMapper.update(null, patientInfoUpdateWrapper);

        if(result == 1){
            return Boolean.TRUE;
        }
        else {
            return Boolean.FALSE;
        }
    }

    @Override
    public Boolean updatePassword(String username, String password) {
        UpdateWrapper<PatientInfo> patientInfoUpdateWrapper = new UpdateWrapper<>();
        patientInfoUpdateWrapper.eq("username",username)
                .set("password", password);

        int result = patientInfoMapper.update(null, patientInfoUpdateWrapper);

        if(result == 1){
            return Boolean.TRUE;
        }
        else {
            return Boolean.FALSE;
        }
    }

    @Override
    public UserDto loadUserByUsername(String username) {
        PatientInfo patientInfo = getAllInfoByName(username);

        if(patientInfo != null) {
            UserDto userDto = new UserDto();
            BeanUtil.copyProperties(patientInfo, userDto);
            return userDto;
        }
        return null;
    }

    @Override
    public String getEmailByName(String username) {
        PatientInfo patientInfo = getAllInfoByName(username);

        if(patientInfo != null) {
            return patientInfo.getEmail();
        }
        return null;
    }
}
