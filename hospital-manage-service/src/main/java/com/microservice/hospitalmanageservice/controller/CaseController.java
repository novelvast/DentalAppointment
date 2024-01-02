package com.microservice.hospitalmanageservice.controller;

import com.microservice.common.api.CommonResult;
import com.microservice.hospitalmanageservice.entity.vo.CaseVo;
import com.microservice.hospitalmanageservice.service.ICaseService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
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
