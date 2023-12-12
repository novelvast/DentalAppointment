package com.microservice.authservice.service;

import com.microservice.common.domain.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 患者信息服务远程调用Service
 *
 * @author zhao
 * @date 2023/12/10
 */
@Service
@FeignClient("personal-info-service")
public interface PatientInfoService {
    @GetMapping("/api/patient/loadByUsername")
    UserDto loadUserByUsername(@RequestParam String username);
}
