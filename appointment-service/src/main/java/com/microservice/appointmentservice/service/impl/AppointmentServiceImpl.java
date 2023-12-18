package com.microservice.appointmentservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microservice.appointmentservice.dto.AppointmentDto;
import com.microservice.appointmentservice.entity.OrderInfo;
import com.microservice.appointmentservice.mapper.OrderInfoMapper;
import com.microservice.appointmentservice.pojo.BookRequest;
import com.microservice.appointmentservice.service.AppointmentService;
import com.microservice.appointmentservice.service.HospitalManageServiceFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//todo:几个次数写到配置文件里热更新
@Slf4j
@Service
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private HospitalManageServiceFeignClient hospitalManageService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public AppointmentDto getAppointmentById(Integer id){
        OrderInfo orderInfo=orderInfoMapper.selectById(id);
        AppointmentDto dto = new AppointmentDto();
        dto.setPatientId(orderInfo.getPatientId());
        dto.setDoctorId(orderInfo.getDoctorId());
        dto.setOrderTime(orderInfo.getOrderTime());
        dto.setClinicTime(orderInfo.getClinicTime());
        dto.setDiseaseDescription(orderInfo.getDiseaseDescription());
        dto.setHospital(orderInfo.getHospital());
        dto.setOrderDepartment(orderInfo.getOrderDepartment());
        return dto;
    }

    public boolean deleteAppointmentById(Integer id){
        int delete=orderInfoMapper.deleteById(id);
        log.warn("delete==>{}", delete);
        return true;
    }
    //写一个预约订单到数据库中
    public Integer book(@RequestBody BookRequest bookRequest) {

        //TODO:是否需要审核
        int orderCountToday=orderInfoMapper.getAppointmentCountByPatientToday(bookRequest.getPatientId());
        if(orderCountToday>3){
            ;//TODO:需要审核则调用审核微服务和消息微服务
        }
        //审核通过或不需要审核

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setPatientId(bookRequest.getPatientId());
        orderInfo.setDoctorId(bookRequest.getDoctorId());
        orderInfo.setOrderTime(bookRequest.getOrderTime());
        orderInfo.setClinicTime(bookRequest.getClinicTime());
        orderInfo.setOrderDepartment(bookRequest.getOrderDepartment());
        orderInfo.setDiseaseDescription(bookRequest.getDiseaseDescription());
        orderInfo.setHospital(bookRequest.getHospital());

        int insert = orderInfoMapper.insert(orderInfo);
        log.warn("insert==>{}", insert);
        hospitalManageService.appointment(orderInfo.getHospital(),orderInfo.getDoctorId(),orderInfo.getPatientId(),orderInfo.getOrderTime(),orderInfo.getClinicTime(),orderInfo.getOrderDepartment(),orderInfo.getDiseaseDescription());
        //TODO:发消息给医院管理，医院管理监听到修改医院数据库

        if (insert == 1) {
            return orderInfo.getId();
        } else {
            return -1;
        }
    }

    public List<AppointmentDto> getAppointmentByPatientId(String patientId) {
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("patient_id", patientId);
        List<OrderInfo> orderInfos=orderInfoMapper.selectList(queryWrapper);

        List<AppointmentDto> result = new ArrayList<>();
        for (OrderInfo orderInfo : orderInfos) {
            AppointmentDto dto = new AppointmentDto();
            dto.setPatientId(orderInfo.getPatientId());
            dto.setDoctorId(orderInfo.getDoctorId());
            dto.setOrderTime(orderInfo.getOrderTime());
            dto.setClinicTime(orderInfo.getClinicTime());
            dto.setDiseaseDescription(orderInfo.getDiseaseDescription());
            dto.setHospital(orderInfo.getHospital());
            dto.setOrderDepartment(orderInfo.getOrderDepartment());
            result.add(dto);
        }
        return result;
    }

    public List<AppointmentDto> getAppointmentByDoctorId(String doctorId) {
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("doctor_id", doctorId);
        List<OrderInfo> orderInfos=orderInfoMapper.selectList(queryWrapper);

        List<AppointmentDto> result = new ArrayList<>();
        for (OrderInfo orderInfo : orderInfos) {
            AppointmentDto dto = new AppointmentDto();
            dto.setPatientId(orderInfo.getPatientId());
            dto.setDoctorId(orderInfo.getDoctorId());
            dto.setOrderTime(orderInfo.getOrderTime());
            dto.setClinicTime(orderInfo.getClinicTime());
            dto.setDiseaseDescription(orderInfo.getDiseaseDescription());
            dto.setHospital(orderInfo.getHospital());
            dto.setOrderDepartment(orderInfo.getOrderDepartment());
        }
        return result;
    }


    private static final String CANCEL_COUNT_KEY_PREFIX = "cancelCount:";
    private static final Integer EXPIRE_TIME_SECONDS = 24 * 60 * 60; // 24 小时
//    private Long getCancelCount(String userId) {
//        String currentDate = LocalDate.now().toString();
//        HashOperations<String, String, Long> hashOperations = redisTemplate.opsForHash();
//        return hashOperations.get(CANCEL_COUNT_KEY_PREFIX + userId, currentDate);
//    }
    public Boolean patientCancel(Integer orderId) {
        OrderInfo orderInfo = orderInfoMapper.selectById(orderId);
        String patientId = orderInfo.getPatientId();
        String currentDate = LocalDate.now().toString();

        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();

        // 检查用户今天的取消预约次数
        String cancelCountStr = hashOperations.get(CANCEL_COUNT_KEY_PREFIX + patientId ,currentDate);
        Long cancelCount = cancelCountStr != null ? Long.parseLong(cancelCountStr) : null;

        System.out.println(hashOperations.get(CANCEL_COUNT_KEY_PREFIX + patientId , currentDate));
        if (cancelCount == null) {
            // 如果用户今天还没有取消预约，则设置取消预约次数为 1，并设置过期时间
            hashOperations.put(CANCEL_COUNT_KEY_PREFIX + patientId , currentDate, "1");
        } else if (cancelCount < 3) {
            // 如果取消预约次数小于 3，则增加取消预约次数
            hashOperations.increment(CANCEL_COUNT_KEY_PREFIX + patientId ,currentDate, 1L);
        } else{
            // 调用审核微服务
            boolean isApproval=true;
            if(isApproval){
                hashOperations.increment(CANCEL_COUNT_KEY_PREFIX + patientId ,currentDate, 1L);
            }else{
                // 如果审核通过就跳出if，失败直接return
                System.out.println(hashOperations.get(CANCEL_COUNT_KEY_PREFIX + patientId , currentDate));
                return false;
            }
        }
        System.out.println(hashOperations.get(CANCEL_COUNT_KEY_PREFIX + patientId , currentDate));
        deleteAppointmentById(orderId);
        return true;
    }

    public Boolean doctorCancel(Integer orderId) {
        boolean isApproval=true;
        //审核
        if(isApproval){
            deleteAppointmentById(orderId);
            return true;
        }else{
            return false;
        }
    }
}
