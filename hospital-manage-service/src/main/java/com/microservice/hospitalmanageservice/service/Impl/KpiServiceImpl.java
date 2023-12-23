package com.microservice.hospitalmanageservice.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.microservice.hospitalmanageservice.service.IKpiService;
import com.microservice.hospitalmanageservice.entity.vo.KpiVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class KpiServiceImpl implements IKpiService {
    private final Environment environment;
    private final RestTemplate restTemplate;

    @Autowired
    public KpiServiceImpl(Environment environment, RestTemplate restTemplate) {
        this.environment = environment;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<KpiVo> getKpiByDoctorId(Integer hospitalId, String doctorId) {
        String url = MessageFormat.format(Objects.requireNonNull(environment.getProperty("api.his" + hospitalId + ".kpi.byDoctorId")), doctorId);
        ResponseEntity<HashMap> responseEntity = restTemplate.getForEntity(url, HashMap.class);
        Object source = Objects.requireNonNull(responseEntity.getBody()).get("data");
        if (source instanceof List) {
            List<?> sourceList = (List<?>) source;
            List<KpiVo> KpiVos = sourceList.stream()
                    .map(element -> BeanUtil.copyProperties(element, KpiVo.class))
                    .collect(Collectors.toList());
            return KpiVos;
        }
        return null;
    }

    @Override
    public KpiVo getKpiByDoctorIdAndDay(Integer hospitalId, String doctorId, LocalDate day) {
        String url = MessageFormat.format(Objects.requireNonNull(environment.getProperty("api.his" + hospitalId + ".kpi.byDoctorIdAndDay")), doctorId, day);
        log.info(url);
        ResponseEntity<HashMap> responseEntity = restTemplate.getForEntity(url, HashMap.class);
        Object source = Objects.requireNonNull(responseEntity.getBody()).get("data");
        return BeanUtil.copyProperties(source, KpiVo.class);
    }

    @Override
    public List<KpiVo> getKpiByDoctorIdAndMonth(Integer hospitalId, String doctorId, String yearMonth) {
        String url = MessageFormat.format(Objects.requireNonNull(environment.getProperty("api.his" + hospitalId + ".kpi.byDoctorIdAndMonth")), doctorId, yearMonth);
        ResponseEntity<HashMap> responseEntity = restTemplate.getForEntity(url, HashMap.class);
        List<?> sourceList = (List<?>) Objects.requireNonNull(responseEntity.getBody()).get("data");
        log.info(sourceList.toString());
        List<KpiVo> kpiVos = sourceList.stream()
                .map(element -> BeanUtil.copyProperties(element, KpiVo.class))
                .collect(Collectors.toList());
        return kpiVos;
    }
}
