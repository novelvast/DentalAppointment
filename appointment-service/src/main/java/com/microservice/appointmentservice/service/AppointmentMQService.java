package com.microservice.appointmentservice.service;

import com.microservice.common.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentMQService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    //无需审核，直接发送消息给管理微服务修改数据库
    public void appointmentIsOk(){
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_TOPIC_RESERVATION, RabbitMQConfig.ROUTING_KEY_MANAGEMENT, "ok");
    }

    //发消息到审核微服务
    public void appointmentNeedApproval(){
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_TOPIC_RESERVATION, RabbitMQConfig.ROUTING_KEY_APPROVAL, "ok");
    }

    //审核得到结果分情况处理
    @RabbitListener(queues = RabbitMQConfig.QUEUE_APPOINTMRNT)
    public void appointmentGotResult(){
        //通过
        //不通过
    }


}
