package com.microservice.hospitalmanageservice.controller;

import com.microservice.common.api.CommonResult;
import com.microservice.hospitalmanageservice.entity.vo.KpiVo;
import com.microservice.hospitalmanageservice.service.IKpiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/{hospitalId}/kpi")
public class KpiController {
    @Resource
    private IKpiService kpiService;

    @GetMapping("doctor/{doctorId}")
    public CommonResult getDoctorKpi(@PathVariable Integer hospitalId, @PathVariable String doctorId) {
        // TODO: 去对应医院调医生kpi
        try {
            List<KpiVo> kpisByDoctorId = kpiService.getKpiByDoctorId(hospitalId, doctorId);
            return CommonResult.success(kpisByDoctorId);
        } catch (Exception exception) {
            return CommonResult.failed(exception.getMessage());
        }
    }

    @GetMapping("doctor/{doctorId}/day/{day}")
    public CommonResult getKpiByDoctorIdAndDay(@PathVariable Integer hospitalId, @PathVariable String doctorId, @PathVariable String day) {
        LocalDate localDate = LocalDate.parse(day);
        log.info("查询id为{}的医生在{}这天的kpi", doctorId, day);
        try {
            KpiVo kpi = kpiService.getKpiByDoctorIdAndDay(hospitalId, doctorId, localDate);
            return CommonResult.success(kpi);
        } catch (Exception exception) {
            return CommonResult.failed(exception.getMessage());
        }
    }

    @GetMapping("doctor/{doctorId}/month/{month}")
    public CommonResult getKpiByDoctorIdAndMonth(@PathVariable Integer hospitalId, @PathVariable String doctorId, @PathVariable String month) {
        try {
            List<KpiVo> kpis = kpiService.getKpiByDoctorIdAndMonth(hospitalId, doctorId, month);
            return CommonResult.success(kpis);
        } catch (Exception exception) {
            return CommonResult.failed(exception.getMessage());
        }
    }
}
