package com.microservice.appointmentservice.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.appointmentservice.dto.*;
import com.microservice.appointmentservice.entity.OrderInfo;
import com.microservice.appointmentservice.mapper.OrderInfoMapper;
import com.microservice.appointmentservice.pojo.BookRequest;
import com.microservice.appointmentservice.pojo.CancelRequest;
import com.microservice.appointmentservice.service.AppointmentMQService;
import com.microservice.appointmentservice.service.AppointmentService;
import com.microservice.appointmentservice.service.HospitalManageFeignService;
import com.microservice.appointmentservice.service.PersonalInfoFeignService;
import com.microservice.common.api.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

//todo:几个次数写到配置文件里热更新
@Slf4j
@Service
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private AppointmentMQService appointmentMQService;

    @Autowired
    private PersonalInfoFeignService personalInfoFeignService;

    @Autowired
    private HospitalManageFeignService hospitalManageFeignService;
    public AppointmentDto getAppointmentById(Integer id){
        OrderInfo orderInfo=orderInfoMapper.selectById(id);
        AppointmentDto dto = new AppointmentDto();
        dto.setId(orderInfo.getId());
        dto.setPatientId(orderInfo.getPatientId());
        //调用信息api，获取用户名
        CommonResult result = personalInfoFeignService.getByName(orderInfo.getPatientId());
        LinkedHashMap data = (LinkedHashMap) result.getData();
        dto.setPatientName((String) data.get("name"));

        dto.setDoctorId(orderInfo.getDoctorId());
        dto.setOrderTime(orderInfo.getOrderTime());
        dto.setClinicTime(orderInfo.getClinicTime());
        dto.setDiseaseDescription(orderInfo.getDiseaseDescription());
        dto.setHospital(orderInfo.getHospital());
        dto.setOrderDepartment(orderInfo.getOrderDepartment());
        dto.setApprovalStatus(orderInfo.getApprovalStatus());
        return dto;
    }

    public Integer deleteAppointmentById(Integer id){
        Integer delete=orderInfoMapper.deleteById(id);
        log.warn("delete==>{}", delete);
        return delete;
    }

    public void addTOManage(AppointmentDto appointmentDto){
        ManageAddDto manageAddDto=new ManageAddDto();
        manageAddDto.setHospital(appointmentDto.getHospital());
        manageAddDto.setDiseaseDescription(appointmentDto.getDiseaseDescription());
        manageAddDto.setPatientId(appointmentDto.getPatientId());
        manageAddDto.setOrderTime(appointmentDto.getOrderTime());
        manageAddDto.setClinicTime(appointmentDto.getClinicTime());
        manageAddDto.setDoctorId(appointmentDto.getDoctorId());

        ManageMQDto manageMQDto=new ManageMQDto();
        manageMQDto.setData(manageAddDto);
        manageMQDto.setType("添加数据");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString="";
        try {
            // 将对象转换为JSON字符串
            jsonString = objectMapper.writeValueAsString(manageMQDto);
            System.out.println(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        appointmentMQService.sendToManage(jsonString);
    }
    //写一个预约订单到数据库中
    public Integer book(@RequestBody BookRequest bookRequest) {

        int orderCountToday=orderInfoMapper.getAppointmentCountByPatientToday(bookRequest.getPatientId());
        //orederInfo和bookRequest匹配
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setPatientId(bookRequest.getPatientId());
        orderInfo.setDoctorId(bookRequest.getDoctorId());
        orderInfo.setOrderTime(LocalDateTime.now());
        orderInfo.setClinicTime(bookRequest.getClinicTime());
        orderInfo.setOrderDepartment(bookRequest.getOrderDepartment());
        orderInfo.setDiseaseDescription(bookRequest.getDiseaseDescription());
        orderInfo.setHospital(bookRequest.getHospital());
        //先插入
        int insert = orderInfoMapper.insert(orderInfo);
        log.warn("insert==>{}", insert);
        //判断是否需要审核
        if(orderCountToday>=3){
            changeApprovalStatus(orderInfo.getId(),"预约待审核");
            //转换成json
            ApprovalDto approvalDto=new ApprovalDto();
            approvalDto.setKind("患者");
            approvalDto.setUsername(orderInfo.getPatientId());
            //医院管理微服务获取管理员
            CommonResult result = hospitalManageFeignService.getHospitalAdministrator(orderInfo.getHospital());
            LinkedHashMap data = (LinkedHashMap) result.getData();
            approvalDto.setAdminUsername((String) data.get("administrator"));
//            approvalDto.setAdminUsername("ly");
            approvalDto.setCancelReason("单日多次预约");
            approvalDto.setAuditStatus("预约待审核");
            approvalDto.setOrderId(orderInfo.getId());
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString="";
            try {
                // 将对象转换为JSON字符串
                jsonString = objectMapper.writeValueAsString(approvalDto);
                System.out.println(jsonString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //发送
            appointmentMQService.appointmentNeedApproval(jsonString);
            return 0;
        }
        //不需要审核，直接结束，发消息给医院
        changeApprovalStatus(orderInfo.getId(),"正常");
        //发消息给医院管理，医院管理监听到修改医院数据库
        addTOManage(getAppointmentById(orderInfo.getId()));
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
            dto.setId(orderInfo.getId());
            dto.setPatientId(orderInfo.getPatientId());
            //调用信息api，获取用户名
            CommonResult commonResult = personalInfoFeignService.getByName(orderInfo.getPatientId());
            LinkedHashMap data = (LinkedHashMap) commonResult.getData();
            dto.setPatientName((String) data.get("name"));
            dto.setDoctorId(orderInfo.getDoctorId());
            dto.setOrderTime(orderInfo.getOrderTime());
            dto.setClinicTime(orderInfo.getClinicTime());
            dto.setDiseaseDescription(orderInfo.getDiseaseDescription());
            dto.setHospital(orderInfo.getHospital());
            dto.setOrderDepartment(orderInfo.getOrderDepartment());
            dto.setApprovalStatus(orderInfo.getApprovalStatus());
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
            dto.setId(orderInfo.getId());
            dto.setPatientId(orderInfo.getPatientId());
            //调用信息api，获取用户名
            CommonResult commonResult = personalInfoFeignService.getByName(orderInfo.getPatientId());
            LinkedHashMap data = (LinkedHashMap) commonResult.getData();
            dto.setPatientName((String) data.get("name"));
            dto.setDoctorId(orderInfo.getDoctorId());
            dto.setOrderTime(orderInfo.getOrderTime());
            dto.setClinicTime(orderInfo.getClinicTime());
            dto.setDiseaseDescription(orderInfo.getDiseaseDescription());
            dto.setHospital(orderInfo.getHospital());
            dto.setOrderDepartment(orderInfo.getOrderDepartment());
            dto.setApprovalStatus(orderInfo.getApprovalStatus());
            result.add(dto);
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
    public Integer patientCancel(CancelRequest cancelRequest) {
        OrderInfo orderInfo = orderInfoMapper.selectById(cancelRequest.getOrderId());
        String patientId = orderInfo.getPatientId();
        String currentDate = LocalDate.now().toString();

        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();

        // 检查用户今天的取消预约次数
        String cancelCountStr = hashOperations.get(CANCEL_COUNT_KEY_PREFIX + patientId ,currentDate);
        Long cancelCount = cancelCountStr != null ? Long.parseLong(cancelCountStr) : null;

        //System.out.println(hashOperations.get(CANCEL_COUNT_KEY_PREFIX + patientId , currentDate));
        if (cancelCount == null) {
            // 如果用户今天还没有取消预约，则设置取消预约次数为 1，并设置过期时间
            hashOperations.put(CANCEL_COUNT_KEY_PREFIX + patientId , currentDate, "1");
            // 设置过期时间为 24 小时后
            stringRedisTemplate.expire(CANCEL_COUNT_KEY_PREFIX + patientId, EXPIRE_TIME_SECONDS, TimeUnit.SECONDS);
        } else if (cancelCount < 3) {
            // 如果取消预约次数小于 3，则增加取消预约次数
            hashOperations.increment(CANCEL_COUNT_KEY_PREFIX + patientId ,currentDate, 1L);
        } else{
            //审核
            changeApprovalStatus(orderInfo.getId(),"取消待审核");
            //转换成json
            ApprovalDto approvalDto=new ApprovalDto();
            approvalDto.setKind("患者");
            approvalDto.setUsername(orderInfo.getPatientId());
            //医院管理微服务获取管理员
            CommonResult result = hospitalManageFeignService.getHospitalAdministrator(orderInfo.getHospital());
            LinkedHashMap data = (LinkedHashMap) result.getData();
            approvalDto.setAdminUsername((String) data.get("administrator"));
//            approvalDto.setAdminUsername("ly");
            approvalDto.setCancelReason(cancelRequest.getCancelReason());
            approvalDto.setAuditStatus("取消待审核");
            approvalDto.setOrderId(orderInfo.getId());
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString="";
            try {
                // 将对象转换为JSON字符串
                jsonString = objectMapper.writeValueAsString(approvalDto);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //发送
            appointmentMQService.appointmentNeedApproval(jsonString);
            return 0;
        }
        //给管理
        deleteTOManage(getAppointmentById(cancelRequest.getOrderId()));
        //System.out.println(hashOperations.get(CANCEL_COUNT_KEY_PREFIX + patientId , currentDate));
        Integer delete= deleteAppointmentById(cancelRequest.getOrderId());

        if (delete == 1) {
            return orderInfo.getId();
        } else {
            return -1;
        }
    }

    public void deleteTOManage(AppointmentDto appointmentDto){
        ManageDeleteDto manageDeleteDto=new ManageDeleteDto();
        manageDeleteDto.setClinicTime(appointmentDto.getClinicTime());
        manageDeleteDto.setPatientId(appointmentDto.getPatientId());
        manageDeleteDto.setDoctorId(appointmentDto.getDoctorId());

        ManageMQDto manageMQDto=new ManageMQDto();
        manageMQDto.setData(manageDeleteDto);
        manageMQDto.setType("删除数据");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString="";
        try {
            // 将对象转换为JSON字符串
            jsonString = objectMapper.writeValueAsString(manageMQDto);
            System.out.println(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        appointmentMQService.sendToManage(jsonString);
    }

    public Boolean doctorCancel(CancelRequest cancelRequest) {
        changeApprovalStatus(cancelRequest.getOrderId(),"取消待审核");
        AppointmentDto appointmentDto=getAppointmentById(cancelRequest.getOrderId());
        ApprovalDto approvalDto=new ApprovalDto();
        approvalDto.setKind("医生");
        approvalDto.setUsername(appointmentDto.getPatientId());
        //医院管理微服务获取管理员
        CommonResult result = hospitalManageFeignService.getHospitalAdministrator(appointmentDto.getHospital());
        LinkedHashMap data = (LinkedHashMap) result.getData();
        approvalDto.setAdminUsername((String) data.get("administrator"));
//            approvalDto.setAdminUsername("ly");
        approvalDto.setCancelReason(cancelRequest.getCancelReason());
        approvalDto.setAuditStatus("取消待审核");
        approvalDto.setOrderId(cancelRequest.getOrderId());
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString="";
        try {
            // 将对象转换为JSON字符串
            jsonString = objectMapper.writeValueAsString(approvalDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //发送
        appointmentMQService.appointmentNeedApproval(jsonString);
        return true;
    }

    public void changeApprovalStatus(Integer orderId,String status)
    {
        UpdateWrapper updateWrapper = new UpdateWrapper();

        updateWrapper.eq("ID", orderId);

        updateWrapper.set("approval_status", status);

        orderInfoMapper.update(null,updateWrapper);
    }
}
