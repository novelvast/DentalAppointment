package com.microservice.personalinfoservice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.microservice.common.api.CommonResult;
import com.microservice.common.domain.UserDto;
import com.microservice.personalinfoservice.dto.DoctorCheckDto;
import com.microservice.personalinfoservice.dto.DoctorDto;
import com.microservice.personalinfoservice.entity.DoctorInfo;
import com.microservice.personalinfoservice.mapper.DoctorInfoMapper;
import com.microservice.personalinfoservice.service.AuthService;
import com.microservice.personalinfoservice.service.DoctorInfoService;
import com.microservice.personalinfoservice.service.HospitalManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DoctorInfoServiceImpl implements DoctorInfoService {

    @Autowired
    private DoctorInfoMapper doctorInfoMapper;

    @Autowired
    private AuthService authService;

    @Autowired
    private HospitalManageService hospitalManageService;

    @Override
    public Boolean register(String username, String password, String phone, String email, Integer hospitalId,
                            String name, Integer jobNumber, String department, String photoUrl) {
        // 查询是否已有该用户
        if (getByName(username) != null){
            return Boolean.FALSE;
        }

        // 调用hospital-manage判断医生工号
        CommonResult<DoctorCheckDto> commonResult = hospitalManageService.getDoctorName(hospitalId, jobNumber);
        if(!Objects.equals(commonResult.getData().getName(), name)) {
            return Boolean.FALSE;
        }

        // 没有对该用户进行添加操作
        DoctorInfo doctorInfo = new DoctorInfo();
        doctorInfo.setUsername(username);
        doctorInfo.setPassword(password);
        doctorInfo.setPhone(phone);
        doctorInfo.setEmail(email);
        doctorInfo.setHospitalId(hospitalId);
        doctorInfo.setName(name);
        doctorInfo.setJobNumber(jobNumber);
        doctorInfo.setDepartment(department);
        doctorInfo.setPhotoUrl(photoUrl);
        int result = doctorInfoMapper.insert(doctorInfo);
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
        params.put("client_id", "client_doctor");
        params.put("client_secret","doctor");
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
    public DoctorDto getById(Integer doctorId) {
        DoctorInfo doctorInfo = doctorInfoMapper.selectById(doctorId);

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
    public Boolean updateInfo(String username, String phone, String email, Integer hospitalId, String name,
                              Integer jobNumber, String department, String photoUrl) {
        UpdateWrapper<DoctorInfo> doctorInfoUpdateWrapper = new UpdateWrapper<>();
        doctorInfoUpdateWrapper.eq("username",username)
                .set("phone", phone)
                .set("email", email)
                .set("hospital_id", hospitalId)
                .set("name", name)
                .set("job_number", jobNumber)
                .set("department", department)
                .set("photo_url", photoUrl);

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

    @Override
    public String getEmailByName(String username) {
        DoctorInfo doctorInfo = getAllInfoByName(username);

        if(doctorInfo != null) {
            return doctorInfo.getEmail();
        }
        return null;
    }

    @Override
    public List<DoctorDto> getByHospital(Integer hospitalId) {
        QueryWrapper<DoctorInfo> doctorDtoQueryWrapper = new QueryWrapper<>();
        doctorDtoQueryWrapper.eq("hospital_id", hospitalId);

        List<DoctorInfo> doctorInfoList = doctorInfoMapper.selectList(doctorDtoQueryWrapper);
        List<DoctorDto> doctorDtoList = new ArrayList<>();

        for (DoctorInfo doctorInfo : doctorInfoList) {
            DoctorDto doctorDto = new DoctorDto();
            BeanUtil.copyProperties(doctorInfo, doctorDto);
            doctorDtoList.add(doctorDto);
        }

        return doctorDtoList;
    }

    @Override
    public  List<DoctorDto> getByDepartment(Integer hospitalId, String department) {
        QueryWrapper<DoctorInfo> doctorInfoQueryWrapper = new QueryWrapper<>();

        doctorInfoQueryWrapper.eq("hospital_id", hospitalId);

        doctorInfoQueryWrapper.eq("department", department);

        List<DoctorInfo> doctorList = doctorInfoMapper.selectList(doctorInfoQueryWrapper);
        List<DoctorDto> doctorDtoList = new ArrayList<>();

        for (DoctorInfo doctorInfo : doctorList) {
            DoctorDto doctorDto = new DoctorDto();
            BeanUtil.copyProperties(doctorInfo, doctorDto);
            doctorDtoList.add(doctorDto);
        }

        return doctorDtoList;
    }
}
