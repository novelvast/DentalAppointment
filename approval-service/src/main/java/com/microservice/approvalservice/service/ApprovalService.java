package com.microservice.approvalservice.service;

import com.microservice.approvalservice.entity.PatientCkInfo;
import com.microservice.common.api.CommonResult;
import org.springframework.stereotype.Service;

@Service
public interface ApprovalService {

    Boolean patient_put(String patientUsername,Integer orderId,String adminUsername,String cancelReason,String auditStatus);

    Boolean doctor_put(String doctorUsername,Integer orderId,String adminUsername,String cancelReason,String auditStatus);

    CommonResult patient_get(String adminUsername);

    CommonResult doctor_get(String adminUsername);
    String check(Integer approvalId,String kind,Integer judge);
}
