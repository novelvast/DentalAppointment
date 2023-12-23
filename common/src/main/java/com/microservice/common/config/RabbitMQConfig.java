package com.microservice.common.config;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // 定义队列名称
    public static final String QUEUE_APPOINTMRNT = "queue_appointment";
    public static final String QUEUE_APPROVAL = "queue_approval";
    public static final String QUEUE_MESSAGE = "queue_message";
    public static final String QUEUE_MANAGEMENT = "queue_management";

    // 定义交换机名称
    public static final String EXCHANGE_TOPIC_RESERVATION = "exchange_topic_reservation";

    // 定义routing key
    public static final String ROUTING_KEY_APPOINTMENT = "appointment.#";
    public static final String ROUTING_KEY_APPROVAL = "approval.#";
    public static final String ROUTING_KEY_MESSAGE = "message.#";
    public static final String ROUTING_KEY_MANAGEMENT = "management.#";

    // 声明交换机
    @Bean(EXCHANGE_TOPIC_RESERVATION)
    public Exchange exchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPIC_RESERVATION).durable(true).build();
    }

    // 声明预约队列
    @Bean(QUEUE_APPOINTMRNT)
    public Queue queueReservation() {
        return new Queue(QUEUE_APPOINTMRNT);
    }

    // 声明审核队列
    @Bean(QUEUE_APPROVAL)
    public Queue queueAudit() {
        return new Queue(QUEUE_APPROVAL);
    }

    // 声明通知队列
    @Bean(QUEUE_MESSAGE)
    public Queue queueNotify() {
        return new Queue(QUEUE_MESSAGE);
    }

    // 声明医院管理队列
    @Bean(QUEUE_MANAGEMENT)
    public Queue queueManagement() {
        return new Queue(QUEUE_MANAGEMENT);
    }

    // 预约队列绑定交换机，指定routingKey
    @Bean
    public Binding bindingQueueReservation(@Qualifier(QUEUE_APPOINTMRNT) Queue queue,
                                           @Qualifier(EXCHANGE_TOPIC_RESERVATION) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_APPOINTMENT).noargs();
    }

    // 审核队列绑定交换机，指定routingKey
    @Bean
    public Binding bindingQueueAudit(@Qualifier(QUEUE_APPROVAL) Queue queue,
                                     @Qualifier(EXCHANGE_TOPIC_RESERVATION) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_APPROVAL).noargs();
    }

    // 通知队列绑定交换机，指定routingKey
    @Bean
    public Binding bindingQueueNotify(@Qualifier(QUEUE_MESSAGE) Queue queue,
                                      @Qualifier(EXCHANGE_TOPIC_RESERVATION) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_MESSAGE).noargs();
    }

    // 医院管理队列绑定交换机，指定routingKey
    @Bean
    public Binding bindingQueueManagement(@Qualifier(QUEUE_MANAGEMENT) Queue queue,
                                          @Qualifier(EXCHANGE_TOPIC_RESERVATION) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_MANAGEMENT).noargs();
    }
}


