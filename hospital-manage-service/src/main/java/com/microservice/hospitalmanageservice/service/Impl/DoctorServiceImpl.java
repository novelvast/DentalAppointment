package com.microservice.hospitalmanageservice.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.microservice.hospitalmanageservice.entity.vo.DoctorVo;
import com.microservice.hospitalmanageservice.service.IDoctorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Override
    public List<DoctorVo> getAll(String hospitalId) {
        String url = Objects.requireNonNull(environment.getProperty("api.his" + hospitalId + ".doctors.all"));
        ResponseEntity<HashMap> responseEntity = restTemplate.getForEntity(url, HashMap.class);
        List<?> source = (List<?>) Objects.requireNonNull(responseEntity.getBody()).get("data");
        log.info(source.toString());
        List<DoctorVo> doctorVos = source.stream()
                .map(element -> BeanUtil.copyProperties(element, DoctorVo.class))
                .collect(Collectors.toList());
        return doctorVos;
    }

}
