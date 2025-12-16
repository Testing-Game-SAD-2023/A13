package com.g2.interfaces;

import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class BaseServiceBroker extends BaseService {

    protected RabbitTemplate rabbitTemplate;
    protected AsyncRabbitTemplate asyncRabbitTemplate;

    protected BaseServiceBroker(RabbitTemplate rabbitTemplate, AsyncRabbitTemplate asyncRabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.asyncRabbitTemplate = asyncRabbitTemplate;
    }
}
