package com.microservice.personalinfoservice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microservice.common.api.CommonResult;
import com.microservice.personalinfoservice.dto.PatientDto;
import com.microservice.personalinfoservice.entity.PatientInfo;
import com.microservice.personalinfoservice.mapper.PatientInfoMapper;
import com.microservice.personalinfoservice.service.PatientInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PatientInfoServiceImpl implements PatientInfoService {

    @Autowired
    private PatientInfoMapper patientInfoMapper;
    @Override
    public Boolean register(String username, String password, String phone, String email, String idNumber, String name, String gender, LocalDate birthday) {
        int result = patientInfoMapper.insert(new PatientInfo(username, password, phone, email, idNumber, name, gender, birthday));
        if(result == 1){
            return Boolean.TRUE;
        }
        else {
            return Boolean.FALSE;
        }
    }

    @Override
    public CommonResult login(String username, String password) {
        return null;
    }

    @Override
    public PatientDto getPatientByName(String username) {
        QueryWrapper<PatientInfo> patientInfoQueryWrapper = new QueryWrapper<>();
        patientInfoQueryWrapper.eq("username", username);

        PatientInfo patientInfo = patientInfoMapper.selectOne(patientInfoQueryWrapper);

        if(patientInfo != null) {
            PatientDto patientDto =new PatientDto();
            BeanUtil.copyProperties(patientInfo, patientDto);
            return patientDto;
        }
        return null;
    }

    @Override
    public Boolean updateInfo(String username, String phone, String email, String IDNumber, String name, String gender, String birthday) {
        return Boolean.FALSE;
    }

    @Override
    public void updatePassword(String username, String password) {

    }
}
