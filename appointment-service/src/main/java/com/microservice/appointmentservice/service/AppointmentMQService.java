package com.microservice.appointmentservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.appointmentservice.dto.AppointmentDto;
import com.microservice.appointmentservice.dto.ApprovalDto;
import com.microservice.appointmentservice.dto.ApprovalMQDto;
import com.microservice.appointmentservice.dto.ApprovalResultDto;
import com.microservice.appointmentservice.entity.OrderInfo;
//import com.microservice.common.config.RabbitMQConfig;
import com.microservice.appointmentservice.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentMQService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AppointmentService appointmentService;
    //无需审核，直接发送消息给管理微服务修改数据库
    public void sendToManage(String info){
        //医院id、就诊时间、医生id、用户id、病情描述。
        //医生id、用户id、就诊时间
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_TOPIC_RESERVATION, RabbitMQConfig.ROUTING_KEY_MANAGEMENT, info);
    }

    //发消息到审核微服务
    public void appointmentNeedApproval(String approvalInfo){
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_TOPIC_RESERVATION, RabbitMQConfig.ROUTING_KEY_APPROVAL, approvalInfo);
    }

    //审核得到结果分情况处理
    @RabbitListener(queues = RabbitMQConfig.QUEUE_APPOINTMRNT)
    public void appointmentGotResult(String result){
        //得到处理结果
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ApprovalMQDto approvalMQDto=objectMapper.readValue(result, ApprovalMQDto.class);
            if(approvalMQDto.getType().equals("审核结果")) {
                // 将 JSON 字符串转化为对象
                String approvalResultString=(String) approvalMQDto.getData();
                ApprovalResultDto approvalResultDto=objectMapper.readValue(approvalResultString, ApprovalResultDto.class);
                //修改状态
                //预约待审核
                AppointmentDto appointmentDto = appointmentService.getAppointmentById(approvalResultDto.getOrderId());
                if (appointmentDto.getApprovalStatus().equals("预约待审核")) {
                    //预约审核通过
                    if (approvalResultDto.getAuditStatus().equals("审核通过")) {
                        appointmentService.changeApprovalStatus(approvalResultDto.getOrderId(), "正常");
                        appointmentService.addTOManage(appointmentService.getAppointmentById(approvalResultDto.getOrderId()));
                    }
                    else
                        appointmentService.changeApprovalStatus(approvalResultDto.getOrderId(), "预约审核不通过");
                }
                if (appointmentDto.getApprovalStatus().equals("取消待审核")) {
                    //预约审核通过
                    if (approvalResultDto.getAuditStatus().equals("审核通过")) {
                        appointmentService.changeApprovalStatus(approvalResultDto.getOrderId(), "取消审核通过");
                        appointmentService.deleteAppointmentById(approvalResultDto.getOrderId());
                        appointmentService.deleteTOManage(appointmentService.getAppointmentById(approvalResultDto.getOrderId()));
                    } else
                        appointmentService.changeApprovalStatus(approvalResultDto.getOrderId(), "正常");
                }

            }
            if(approvalMQDto.getType().equals("结果信息")){
                String resultInfo=(String) approvalMQDto.getData();
                System.out.println(resultInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
