package com.microservice.personalinfoservice.service;

import com.microservice.common.api.CommonResult;
import com.microservice.personalinfoservice.dto.DoctorCheckDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



/**
 * 医院管理服务远程调用
 *
 * @author zhao
 * @date 2023/12/23
 */
@FeignClient("hospital-manage-service")
public interface HospitalManageService {

    @GetMapping(value = "api/hospital/{hospitalId}/doctor/id/{jobNumber}")
    CommonResult<DoctorCheckDto> getDoctorName(@PathVariable("hospitalId") Integer hospitalId, @PathVariable("jobNumber") Integer jobNumber);
}
