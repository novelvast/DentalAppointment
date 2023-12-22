package com.microservice.approvalservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.approvalservice.entity.RabbitResult;
import com.microservice.common.api.CommonResult;
import com.microservice.common.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ApprovalMQService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    ApprovalService approvalService;
    //监听队列，收到预约微服务的信息
    @RabbitListener(queues = RabbitMQConfig.QUEUE_APPROVAL)
    public void gotApprovalInfo(String messagebody){

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(messagebody);

            // 解析 json
            String Username = jsonNode.get("Username").asText();
            Integer orderId=jsonNode.get("orderId").asInt();
            String adminUsername = jsonNode.get("adminUsername").asText();
            String cancelReason = jsonNode.get("cancelReason").asText();
            String auditStatus = jsonNode.get("auditStatus").asText();
            String kind = jsonNode.get("kind").asText();

            approvalService.rabbit_put(Username,orderId,adminUsername,cancelReason,auditStatus,kind);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //审核完毕将结果发送给预约微服务
    public void sendResult(String result){
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_TOPIC_RESERVATION,
                RabbitMQConfig.ROUTING_KEY_APPOINTMENT,
                result);
    }
}
