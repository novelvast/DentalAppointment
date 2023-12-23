package com.microservice.hospitalmanageservice.service;

import com.microservice.hospitalmanageservice.entity.vo.CaseVo;

import java.util.List;

public interface ICaseService {
    List<CaseVo> getMedicalRecordByPatientId(String hospitalId, String patientId);
}
