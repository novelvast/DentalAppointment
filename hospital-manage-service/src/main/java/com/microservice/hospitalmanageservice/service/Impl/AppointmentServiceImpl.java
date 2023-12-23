package com.microservice.hospitalmanageservice.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.microservice.hospitalmanageservice.service.IAppointmentService;
import com.microservice.hospitalmanageservice.entity.dto.AppointmentDto;
import com.microservice.hospitalmanageservice.entity.vo.AppointmentVo;
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
public class AppointmentServiceImpl implements IAppointmentService {
    private final Environment environment;
    private final RestTemplate restTemplate;

    @Autowired
    public AppointmentServiceImpl(Environment environment, RestTemplate restTemplate) {
        this.environment = environment;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<AppointmentVo> getByDoctorIdAndDay(String hospitalId, String doctorId, String day) {
        String url = MessageFormat.format(Objects.requireNonNull(environment.getProperty("api.his" + hospitalId + ".appointment.byDoctorIdAndDay")), doctorId, day);
        ResponseEntity<HashMap> responseEntity = restTemplate.getForEntity(url, HashMap.class);
        List<?> sourceList = (List<?>) Objects.requireNonNull(responseEntity.getBody()).get("data");
        List<AppointmentVo> appointmentVos = sourceList.stream()
                .map(element -> BeanUtil.copyProperties(element, AppointmentVo.class))
                .collect(Collectors.toList());
        return appointmentVos;
    }

    @Override
    public void add(String hospitalId, AppointmentDto appointmentDto) {
        //TODO：根据病人的id调用病人的详细信息

        appointmentDto.setDeptId(11);
    }

    @Override
    public void removeById(String hospitalId, String id) {
        String url = MessageFormat.format(Objects.requireNonNull(environment.getProperty("api.his" + hospitalId + ".appointment.deleteById")),id);
        restTemplate.delete(url);
    }
}