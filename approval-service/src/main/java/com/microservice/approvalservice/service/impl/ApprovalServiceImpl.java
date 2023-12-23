package com.microservice.approvalservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.microservice.approvalservice.entity.CheckResult;
import com.microservice.approvalservice.entity.DoctorCkInfo;
import com.microservice.approvalservice.entity.PatientCkInfo;
import com.microservice.approvalservice.entity.RabbitResult;
import com.microservice.approvalservice.list_to_json;
import com.microservice.approvalservice.mapper.DoctorCkInfoMapper;
import com.microservice.approvalservice.mapper.PatientCkInfoMapper;
import com.microservice.approvalservice.service.ApprovalMQService;
import com.microservice.approvalservice.service.ApprovalService;
import com.microservice.common.api.CommonResult;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ApprovalServiceImpl implements ApprovalService {

    @Autowired
    PatientCkInfoMapper patientCkInfoMapper;
    @Autowired
    DoctorCkInfoMapper doctorCkInfoMapper;
    @Autowired
    FeignServiceImpl mess;
    @Autowired
    ApprovalMQService approvalMQService;

    @Override
    public Boolean patient_put(String patientUsername,Integer orderId,String adminUsername,String cancelReason,String auditStatus){
        PatientCkInfo patientCkInfo=new PatientCkInfo();
        patientCkInfo.setPatientUsername(patientUsername);
        patientCkInfo.setOrderId(orderId);
        patientCkInfo.setAdminUsername(adminUsername);
        patientCkInfo.setCancelReason(cancelReason);
        patientCkInfo.setAuditStatus(auditStatus);
        int result = patientCkInfoMapper.insert(patientCkInfo);
        if(result==1){
            return Boolean.TRUE;
        }
        else{
            return Boolean.FALSE;
        }
    }

    @Override
    public Boolean doctor_put(String doctorUsername,Integer orderId,String adminUsername,String cancelReason,String auditStatus){
        DoctorCkInfo doctorCkInfo=new DoctorCkInfo();
        doctorCkInfo.setDoctorUsername(doctorUsername);
        doctorCkInfo.setOrderId(orderId);
        doctorCkInfo.setAdminUsername(adminUsername);
        doctorCkInfo.setCancelReason(cancelReason);
        doctorCkInfo.setAuditStatus(auditStatus);
        int result = doctorCkInfoMapper.insert(doctorCkInfo);
        if(result==1){
            return Boolean.TRUE;
        }
        else{
            return Boolean.FALSE;
        }
    }

    @Override
    public void rabbit_put(String Username,Integer orderId,String adminUsername,String cancelReason,String auditStatus,String kind) {
        RabbitResult rabbitResult=new RabbitResult();
        if(Objects.equals(kind, "医生")){
            boolean judge =doctor_put(Username,orderId,adminUsername,cancelReason,auditStatus);
            if(judge==Boolean.TRUE){
                rabbitResult.setData("插入数据库成功");
                rabbitResult.setType("结果信息");
            }
            else {
                rabbitResult.setData("插入数据库失败");
                rabbitResult.setType("结果信息");
            }
        }
        else if(Objects.equals(kind, "患者")){
            boolean judge =patient_put(Username,orderId,adminUsername,cancelReason,auditStatus);
            if(judge==Boolean.TRUE){
                rabbitResult.setData("插入数据库成功");
                rabbitResult.setType("结果信息");
            }
            else{
                rabbitResult.setData("插入数据库失败");
                rabbitResult.setType("结果信息");
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr;
        try {
            jsonStr = objectMapper.writeValueAsString(rabbitResult);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        approvalMQService.sendResult(jsonStr);
    }
    @Override
    public CommonResult patient_get(String adminUsername){
        List<PatientCkInfo> patientCkInfoList= patientCkInfoMapper.find_patient(adminUsername);
        return CommonResult.success(patientCkInfoList);
    }

    @Override
    public CommonResult doctor_get(String adminUsername){
        List<DoctorCkInfo> doctorCkInfoList= doctorCkInfoMapper.find(adminUsername);
        return CommonResult.success(doctorCkInfoList);
    }
    @GlobalTransactional
    @Override
    public String check(Integer approvalId,String kind,Integer judge){
        if(judge==1){
            if(Objects.equals(kind, "患者")){
                patientCkInfoMapper.update("审核通过",approvalId);
                PatientCkInfo patientCkInfo=patientCkInfoMapper.find_message_patient(approvalId);
                patient_json_send(patientCkInfo);
            }
            else if(Objects.equals(kind, "医生")){
                doctorCkInfoMapper.update("审核通过",approvalId);
                DoctorCkInfo doctorCkInfo=doctorCkInfoMapper.find_message(approvalId);
                doctor_json_send(doctorCkInfo);
            }

            CheckResult c=new CheckResult();
            c.setStatus("approved");
            c.setNotes("Approval granted for the request");
            List<CheckResult> checkResult=new ArrayList<>();
            checkResult.add(c);
            list_to_json List=new list_to_json();
            return List.convertListToJson(checkResult);
        }
        else{
            if(Objects.equals(kind, "患者")){
                patientCkInfoMapper.update("审核不通过",approvalId);
                PatientCkInfo patientCkInfo=patientCkInfoMapper.find_message_patient(approvalId);
                patient_json_send(patientCkInfo);
            }
            else if(Objects.equals(kind, "医生")){
                doctorCkInfoMapper.update("审核不通过",approvalId);
                DoctorCkInfo doctorCkInfo=doctorCkInfoMapper.find_message(approvalId);
                doctor_json_send(doctorCkInfo);
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

    public void doctor_json_send(DoctorCkInfo entity) {
        // 使用 ObjectMapper 转换为 JSON 字符串
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonString = objectMapper.writeValueAsString(entity);
            ObjectMapper objectMapper2 = new ObjectMapper();
            JsonNode jsonNode = objectMapper2.readTree(jsonString);

            // 修改属性名
            ((ObjectNode) jsonNode).put("username", jsonNode.get("doctorUsername"));
            ((ObjectNode) jsonNode).remove("doctorUsername");
            ((ObjectNode) jsonNode).put("kind", "医生");

            // 将修改后的 JsonNode 转换为 JSON 字符串
            String modifiedJsonString = objectMapper2.writeValueAsString(jsonNode);

            ObjectMapper objectMapper3 = new ObjectMapper();
            RabbitResult rabbitResult=new RabbitResult();
            rabbitResult.setData(modifiedJsonString);
            rabbitResult.setType("审核结果");
            String jsonStr;
            try {
                jsonStr = objectMapper3.writeValueAsString(rabbitResult);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            approvalMQService.sendResult(jsonStr);
            System.out.println( mess.send(modifiedJsonString));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void patient_json_send(PatientCkInfo entity) {
        // 使用 ObjectMapper 转换为 JSON 字符串
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonString = objectMapper.writeValueAsString(entity);
            ObjectMapper objectMapper2 = new ObjectMapper();
            JsonNode jsonNode = objectMapper2.readTree(jsonString);

            // 修改属性名
            ((ObjectNode) jsonNode).put("username", jsonNode.get("patientUsername"));
            ((ObjectNode) jsonNode).remove("patientUsername");
            ((ObjectNode) jsonNode).put("kind", "患者");

            // 将修改后的 JsonNode 转换为 JSON 字符串
            String modifiedJsonString = objectMapper2.writeValueAsString(jsonNode);

            ObjectMapper objectMapper3 = new ObjectMapper();
            RabbitResult rabbitResult=new RabbitResult();
            rabbitResult.setData(modifiedJsonString);
            rabbitResult.setType("审核结果");
            String jsonStr;
            try {
                jsonStr = objectMapper3.writeValueAsString(rabbitResult);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            approvalMQService.sendResult(jsonStr);
            System.out.println( mess.send(modifiedJsonString));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}