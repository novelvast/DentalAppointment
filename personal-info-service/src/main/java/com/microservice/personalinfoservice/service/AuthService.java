package com.microservice.personalinfoservice.service;

import com.microservice.common.domain.Oauth2TokenDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 认证服务远程调用
 *
 * @author zhao
 * @date 2023/12/13
 */
@FeignClient("auth-service")
public interface AuthService {

    @PostMapping(value = "/oauth/token")
    Oauth2TokenDto getAccessToken(@RequestParam Map<String, String> parameters);

}
