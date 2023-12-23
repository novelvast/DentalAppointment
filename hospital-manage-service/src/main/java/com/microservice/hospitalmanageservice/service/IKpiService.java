package com.microservice.hospitalmanageservice.service;

import com.microservice.hospitalmanageservice.entity.vo.KpiVo;

import java.time.LocalDate;
import java.util.List;

public interface IKpiService {
    List<KpiVo> getKpiByDoctorId(Integer hospitalId, String doctorId);

    KpiVo getKpiByDoctorIdAndDay(Integer hospitalId, String doctorId, LocalDate day);

    List<KpiVo> getKpiByDoctorIdAndMonth(Integer hospitalId, String doctorId, String yearMonth);
}
