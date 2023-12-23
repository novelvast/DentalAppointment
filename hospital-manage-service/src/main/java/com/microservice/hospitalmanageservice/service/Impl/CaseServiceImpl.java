package com.microservice.hospitalmanageservice.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.microservice.hospitalmanageservice.service.ICaseService;
import com.microservice.hospitalmanageservice.entity.vo.CaseVo;
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
public class CaseServiceImpl implements ICaseService {
    private final Environment environment;
    private final RestTemplate restTemplate;

    @Autowired
    public CaseServiceImpl(Environment environment, RestTemplate restTemlate) {
        this.environment = environment;
        this.restTemplate = restTemlate;
    }

    @Override
    public List<CaseVo> getMedicalRecordByPatientId(String hospitalId, String patientId) {
        String url = MessageFormat.format(Objects.requireNonNull(environment.getProperty("api.his" + hospitalId + ".case.byPatientId")),patientId);
        ResponseEntity<HashMap> responseEntity = restTemplate.getForEntity(url, HashMap.class);
        List<?> sourceList = (List<?>) Objects.requireNonNull(responseEntity.getBody()).get("data");
        List<CaseVo> caseVos = sourceList.stream()
                .map(element -> BeanUtil.copyProperties(element, CaseVo.class))
                .collect(Collectors.toList());
        return caseVos;
    }
}
