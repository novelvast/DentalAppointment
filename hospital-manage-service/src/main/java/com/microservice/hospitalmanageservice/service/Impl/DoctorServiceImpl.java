package com.microservice.hospitalmanageservice.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.microservice.hospitalmanageservice.service.IDoctorService;
import com.microservice.hospitalmanageservice.entity.vo.DoctorVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Objects;

@Slf4j
@Service
public class DoctorServiceImpl implements IDoctorService {

    private final Environment environment;
    private final RestTemplate restTemplate;

    @Autowired
    public DoctorServiceImpl(Environment environment, RestTemplate restTemplate) {
        this.environment = environment;
        this.restTemplate = restTemplate;
    }

    @Override
    public DoctorVo getById(Integer hospitalId, String doctorId) {
        String url = MessageFormat.format(Objects.requireNonNull(environment.getProperty("api.his" + hospitalId + ".doctors.getById")), doctorId);
        log.info("url为{}", url);
        ResponseEntity<HashMap> responseEntity = restTemplate.getForEntity(url, HashMap.class);
        Object source = Objects.requireNonNull(responseEntity.getBody()).get("data");
        return BeanUtil.copyProperties(source, DoctorVo.class);
    }

}
