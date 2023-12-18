package com.microservice.hospitalmanageservice.service;

import com.microservice.common.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManageMQService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    //监听队列，收到预约微服务的信息
    @RabbitListener(queues = RabbitMQConfig.QUEUE_APPROVAL)
    public void listenFromApproval(){
        //修改数据库
    }

    //修改完毕，如果失败，发消息给预约，来回退
    public void sendToAppointment(){
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_TOPIC_RESERVATION, RabbitMQConfig.ROUTING_KEY_APPOINTMENT, "ok");
    }
}
