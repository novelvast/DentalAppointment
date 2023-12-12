package com.microservice.approvalservice.controller;

import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.C;
import com.microservice.approvalservice.entity.CheckResult;
import com.microservice.approvalservice.entity.DoctorCkInfo;
import com.microservice.approvalservice.entity.PatientCkInfo;
import com.microservice.approvalservice.list_to_json;
import com.microservice.approvalservice.mapper.DoctorCkInfoMapper;
import com.microservice.approvalservice.mapper.PatientCkInfoMapper;
import com.microservice.approvalservice.service.message;
import com.microservice.approvalservice.service.messageuser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController()
@RequestMapping("/api/approval")
public class approval_controller {

    @Autowired
    PatientCkInfoMapper patientCkInfoMapper;
    @Autowired
    DoctorCkInfoMapper doctorCkInfoMapper;
    @Autowired
    message mess;
    @Autowired
    messageuser user;
/*
* 插入数据
*
* */
    @PutMapping("/patient/insert")
    public String patient_put(String patientUsername,Integer orderId,String adminUsername,String cancelReason,String auditStatus){
        PatientCkInfo patientCkInfo=new PatientCkInfo();
        patientCkInfo.setPatientUsername(patientUsername);
        patientCkInfo.setOrderId(orderId);
        patientCkInfo.setAdminUsername(adminUsername);
        patientCkInfo.setCancelReason(cancelReason);
        patientCkInfo.setAuditStatus(auditStatus);
        patientCkInfoMapper.save(patientCkInfo);
        return"Put_Patient";
    }

    @PutMapping("/doctor/insert")
    public String doctor_put(String doctorUsername,Integer orderId,String adminUsername,String cancelReason,String auditStatus){
        DoctorCkInfo doctorCkInfo=new DoctorCkInfo();
        doctorCkInfo.setDoctorUsername(doctorUsername);
        doctorCkInfo.setOrderId(orderId);
        doctorCkInfo.setAdminUsername(adminUsername);
        doctorCkInfo.setCancelReason(cancelReason);
        doctorCkInfo.setAuditStatus(auditStatus);
        doctorCkInfoMapper.save(doctorCkInfo);
        return"Put_Doctor";
    }
    @GetMapping("/patient")
    public String patient_get(){
        List<PatientCkInfo> patientCkInfoList= patientCkInfoMapper.find();
        System.out.println("开始");
        list_to_json List=new list_to_json();

        System.out.println(user.user(1));
        System.out.println(List.convertListToJson(patientCkInfoList));
        System.out.println(mess.use());
        return "查询患者";
    }
/*
*
*
*
*
*
* 上文测试，正式需改动
*
* 改审核消息通知“user”
*
*
*
*
*
* */
    @GetMapping("/doctor")
    public String doctor_get(){
        List<DoctorCkInfo> doctorCkInfoList= doctorCkInfoMapper.find();
        System.out.println("开始");
        list_to_json List=new list_to_json();
        System.out.println(List.convertListToJson(doctorCkInfoList));
        return "查询医生";
    }
/*
*
* 审核通过条件需改动
* */
    @PutMapping("/checkresult")
    public String check(int approvalId,String kind,Integer judge){
        if(judge==1){
            if(Objects.equals(kind, "患者")){
                patientCkInfoMapper.update("审核通过",approvalId);
            }
            else if(Objects.equals(kind, "医生")){
                doctorCkInfoMapper.update("审核通过",approvalId);
            }

            CheckResult c=new CheckResult();
            c.setStatus("approved");
            c.setNotes("Approval granted for the request");
            List<CheckResult> checkResult=new ArrayList<>();
            checkResult.add(c);
            list_to_json List=new list_to_json();
            System.out.println(List.convertListToJson(checkResult));
            return List.convertListToJson(checkResult);
        }
        else{
            if(Objects.equals(kind, "患者")){
                patientCkInfoMapper.update("审核不通过",approvalId);
            }
            else if(Objects.equals(kind, "医生")){
                doctorCkInfoMapper.update("审核不通过",approvalId);
            }

            CheckResult c=new CheckResult();
            c.setStatus("rejected");
            c.setNotes("Approval denied for the request");
            List<CheckResult> checkResult=new ArrayList<>();
            checkResult.add(c);
            list_to_json List=new list_to_json();
            System.out.println(List.convertListToJson(checkResult));
            return List.convertListToJson(checkResult);
        }
    }
}
