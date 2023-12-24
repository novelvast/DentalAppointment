package com.microservice.hospitalmanageservice.service;

import com.microservice.hospitalmanageservice.entity.vo.DoctorVo;

import java.util.List;

public interface IDoctorService {
    DoctorVo getById(Integer hospitalId, String doctorId);
    List<DoctorVo> getAll(String hospitalId);
}
