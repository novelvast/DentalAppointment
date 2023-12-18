package com.microservice.approvalservice.service;

import com.microservice.common.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApprovalMQService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    //监听队列，收到预约微服务的信息
    @RabbitListener(queues = RabbitMQConfig.QUEUE_APPROVAL)
    public void gotApprovalInfo(){

    }

    //审核完毕将结果发送给预约微服务
    public void sendResult(){
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_TOPIC_RESERVATION, RabbitMQConfig.ROUTING_KEY_APPOINTMENT, "ok");
    }
}
