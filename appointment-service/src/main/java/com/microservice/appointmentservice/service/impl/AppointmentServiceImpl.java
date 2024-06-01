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
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
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
            HospitalVo data = (HospitalVo) result.getData();

            System.out.println(data.getAdministrator());
            Integer adminId = data.getAdministrator();
            result = personalInfoFeignService.getAdminById(adminId);
            AdminDto adminDto=(AdminDto)result.getData();
            approvalDto.setAdminUsername(adminDto.getUsername());

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
            HospitalVo data = (HospitalVo) result.getData();

            System.out.println(data.getAdministrator());
            Integer adminId = data.getAdministrator();
            result = personalInfoFeignService.getAdminById(adminId);
            AdminDto adminDto=(AdminDto)result.getData();
            approvalDto.setAdminUsername(adminDto.getUsername());
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
        manageDeleteDto.setHospital(appointmentDto.getHospital());

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
        approvalDto.setUsername(appointmentDto.getDoctorId());
        //医院管理微服务获取管理员
        CommonResult result = hospitalManageFeignService.getHospitalAdministrator(appointmentDto.getHospital());
        HospitalVo data = (HospitalVo) result.getData();

        System.out.println(data.getAdministrator());
        Integer adminId = data.getAdministrator();
        result = personalInfoFeignService.getAdminById(adminId);
        AdminDto adminDto=(AdminDto)result.getData();
        approvalDto.setAdminUsername(adminDto.getUsername());
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

    //医生取消时新分配医生
    public Boolean allocate(Integer orderId){
        //TODO:1.错误处理，2.接入外部医院数据库
        //保存分配前订单数据
        AppointmentDto preAppointment= getAppointmentById(orderId);
        //寻找空闲医生
        List<DoctorDto> doctorDtoList=searchFreeDoctor(preAppointment.getHospital(),preAppointment.getOrderDepartment(),preAppointment.getDoctorId());
        int best=aiAllocate(doctorDtoList);
        DoctorDto bestDoctor=doctorDtoList.get(best);
        OrderInfo newOrder=new OrderInfo();
        newOrder.setDoctorId(bestDoctor.getUsername());
        newOrder.setPatientId(preAppointment.getPatientId());
        newOrder.setOrderTime(preAppointment.getOrderTime());
        newOrder.setClinicTime(preAppointment.getClinicTime());
        newOrder.setOrderDepartment(preAppointment.getOrderDepartment());
        newOrder.setDiseaseDescription(preAppointment.getDiseaseDescription());
        newOrder.setHospital(preAppointment.getHospital());
        newOrder.setApprovalStatus("重新分配医生");
        //删除原本的
        deleteAppointmentById(orderId);
        //插入新加的
        orderInfoMapper.insert(newOrder);
        return true;
    }

    public List<DoctorDto> searchFreeDoctor(String hospital,String department,String doctorId){
        Integer hospitalId;
        try {
            hospitalId = Integer.valueOf(hospital);
        } catch (NumberFormatException e) {
            // 处理转换异常，例如记录日志或抛出异常
            System.out.println("Invalid hospital ID format: " + hospital);
            return null;
        }
        // 调用 personalInfoFeignService 的 getByDepartment 方法
        CommonResult response= personalInfoFeignService.getByDepartment(department, hospitalId);
        List<DoctorDto> doctorDtoList=(List<DoctorDto>)response.getData();
        List<DoctorDto> res=new ArrayList<>();
        for (DoctorDto doctorDto : doctorDtoList) {
            String doctorUserName=doctorDto.getUsername();
            if(!doctorUserName.equals(doctorId)){
                res.add(doctorDto);
            }
        }
        return res;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public Integer aiAllocate(List<DoctorDto> doctorDtoList){
        // Python后端接口的URL
        String pythonEndpointUrl = "http://localhost:5000";
        // 创建一个RestTemplate实例
        RestTemplate restTemplate = new RestTemplate();
        // 准备请求参数
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 设置请求体参数
        String requestBody ="{\"doctorDtoList\":\""+doctorDtoList+"\"}";
        // 创建HttpEntity对象
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        // 发送POST请求到后端接口，并获取响应
        String response = restTemplate.postForObject(pythonEndpointUrl, requestEntity, String.class);
        return Integer.parseInt(response);
    }
}
