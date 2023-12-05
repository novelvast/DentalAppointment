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

@Service
public class PatientInfoServiceImpl implements PatientInfoService {

    @Autowired
    private PatientInfoMapper patientInfoMapper;
    @Override
    public void register(String username, String password, String phone, String email, String IDNumber, String name, String gender, String birthday) {

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
        System.out.println("null");
        return null;
    }

    @Override
    public void updateInfo(String username, String phone, String email, String IDNumber, String name, String gender, String birthday) {

    }

    @Override
    public void updatePassword(String username, String password) {

    }
}
