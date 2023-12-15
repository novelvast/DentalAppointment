package com.microservice.approvalservice.controller;

import com.microservice.approvalservice.entity.CheckResult;
import com.microservice.approvalservice.entity.DoctorCkInfo;
import com.microservice.approvalservice.entity.PatientCkInfo;
import com.microservice.approvalservice.list_to_json;
import com.microservice.approvalservice.mapper.DoctorCkInfoMapper;
import com.microservice.approvalservice.mapper.PatientCkInfoMapper;
import com.microservice.approvalservice.service.ApprovalService;
import com.microservice.approvalservice.service.FeignService;
import com.microservice.approvalservice.service.impl.FeignServiceImpl;
import com.microservice.common.api.CommonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController()
@RequestMapping("/api/approval")
public class ApprovalController {
    @Autowired
    ApprovalService approvalService;
    /*
     * 插入数据
     *
     * */
    @ApiOperation("插入患者信息")
    @PutMapping("/patient/insert")
    public CommonResult patient_put(@RequestParam String patientUsername,
                                    @RequestParam Integer orderId,
                                    @RequestParam String adminUsername,
                                    @RequestParam String cancelReason,
                                    @RequestParam String auditStatus) {
        boolean judge = approvalService.patient_put(patientUsername, orderId, adminUsername, cancelReason, auditStatus);
        if (judge == Boolean.TRUE) {
            return CommonResult.success(null, "添加患者信息成功");
        } else {
            return CommonResult.failed("添加患者信息失败");
        }
    }
    @ApiOperation("插入医生信息")
    @PutMapping("/doctor/insert")
    public CommonResult doctor_put(@RequestParam String doctorUsername,
                                   @RequestParam Integer orderId,
                                   @RequestParam String adminUsername,
                                   @RequestParam String cancelReason,
                                   @RequestParam String auditStatus){
        boolean judge = approvalService.doctor_put(doctorUsername, orderId, adminUsername, cancelReason, auditStatus);
        if (judge == Boolean.TRUE) {
            return CommonResult.success(null, "添加医生信息成功");
        } else {
            return CommonResult.failed("添加医生信息失败");
        }
    }
    @ApiOperation("获得某管理员的患者审核数")
    @GetMapping("/patient")
    public CommonResult patient_get(@RequestParam String adminUsername){
        return approvalService.patient_get(adminUsername);
    }
    @ApiOperation("获得某管理员的医生审核数")
    @GetMapping("/doctor")
    public CommonResult doctor_get(@RequestParam String adminUsername){
        return approvalService.doctor_get(adminUsername);
    }
    /*
     *
     * 审核通过条件需改动
     * */
    @ApiOperation("更改审核并发送信息")
    @PutMapping("/checkresult")
    public String check(@RequestParam Integer approvalId,
                        @RequestParam String kind,
                        @RequestParam Integer judge){
        return approvalService.check(approvalId,kind,judge);
    }
}
