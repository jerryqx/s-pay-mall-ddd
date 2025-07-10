package com.qx.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

//@Configuration
public class RabbitMQConfig {

    /**
     * 消费拼团消息
     */
    @Bean
    public Binding topicTeamSuccessBinding(
            @Value("${spring.rabbitmq.config.consumer.topic_team_success.exchange}") String exchangeName,
            @Value("${spring.rabbitmq.config.consumer.topic_team_success.routing_key}") String routingKey,
            @Value("${spring.rabbitmq.config.consumer.topic_team_success.queue}") String queue) {

        // 消息生产方的交换机
        TopicExchange topicExchange = new TopicExchange(exchangeName, true, false);

        return BindingBuilder.bind(new Queue(queue, true))
                .to(topicExchange)
                .with(routingKey);
    }

}
