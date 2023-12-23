package com.microservice.hospitalmanageservice.service;

import com.microservice.hospitalmanageservice.entity.vo.DoctorVo;

public interface IDoctorService {
    DoctorVo getById(Integer hospitalId, String doctorId);

}
