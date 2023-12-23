package com.microservice.hospitalmanageservice.controller;

import com.microservice.common.api.CommonResult;
import com.microservice.hospitalmanageservice.service.ICaseService;
import com.microservice.hospitalmanageservice.entity.vo.CaseVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("{hospitalId}/")
public class CaseController {
    @Resource
    private ICaseService caseService;

    @GetMapping("medical-record/{patientId}")
    public CommonResult getMedicalRecordByPatientId(@PathVariable String hospitalId, @PathVariable String patientId) {
        try {
            List<CaseVo> caseVos = caseService.getMedicalRecordByPatientId(hospitalId, patientId);
            return CommonResult.success(caseVos);
        } catch (Exception exception) {
            return CommonResult.failed(exception.getMessage());
        }
    }
}
