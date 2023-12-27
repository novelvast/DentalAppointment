package com.microservice.hospitalmanageservice.service;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.common.config.RabbitMQConfig;
import com.microservice.hospitalmanageservice.entity.dto.AppointmentDto;
import com.microservice.hospitalmanageservice.entity.dto.MQDto;
import com.microservice.hospitalmanageservice.entity.dto.ManageAddDto;
import com.microservice.hospitalmanageservice.entity.dto.ManageDeleteDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class ManageMQService {
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private IAppointmentService appointmentService;

    //监听队列，收到预约微服务的信息
    @RabbitListener(queues = RabbitMQConfig.QUEUE_MANAGEMENT)
    public void listenFromApproval(String result) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            MQDto mqDto = objectMapper.readValue(result, MQDto.class);
            if (mqDto.getType().equals("添加数据")) {
                // 将 JSON 字符串转化为对象
                ManageAddDto manageAddDto = BeanUtil.copyProperties(mqDto.getData(), ManageAddDto.class);
//                ManageAddDto manageAddDto = objectMapper.readValue(addString, ManageAddDto.class);
                // 添加数据
                log.info("成功接受{}",manageAddDto.toString());
                AppointmentDto appointmentDto = new AppointmentDto();
                appointmentDto.setDoctorId(manageAddDto.getDoctorId());
                appointmentDto.setUserName(manageAddDto.getPatientId());
                appointmentDto.setAppointmentDateTime(manageAddDto.getClinicTime());
                appointmentDto.setPatientCondition(manageAddDto.getDiseaseDescription());
                appointmentService.add(manageAddDto.getHospital(),appointmentDto);
            }
            if (mqDto.getType().equals("删除数据")) {
                ManageDeleteDto manageDeleteDto = BeanUtil.copyProperties(mqDto.getData(), ManageDeleteDto.class);
                log.info("成功接收{}",manageDeleteDto.toString());
                AppointmentDto appointmentDto = new AppointmentDto();
                appointmentDto.setDoctorId(manageDeleteDto.getDoctorId());
                appointmentDto.setUserName(manageDeleteDto.getPatientId());
                appointmentDto.setAppointmentDateTime(manageDeleteDto.getClinicTime());
                appointmentService.modify(manageDeleteDto.getHospital(),appointmentDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
