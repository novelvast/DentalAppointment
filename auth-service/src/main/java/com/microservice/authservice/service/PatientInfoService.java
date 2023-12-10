package com.microservice.authservice.service;

import com.microservice.common.domain.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 前台会员服务远程调用Service
 * Created by macro on 2020/7/16.
 */
@Service
@FeignClient("personal-info-service")
public interface PatientInfoService {
    @GetMapping("/api/patient/loadByUsername")
    UserDto loadUserByUsername(@RequestParam String username);
}
