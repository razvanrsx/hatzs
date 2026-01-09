package com.example.websocket.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {

    public static final String OVERCONSUMPTION_EXCHANGE = "overconsumption.exchange";
    public static final String OVERCONSUMPTION_ROUTING_KEY = "overconsumption.routing-key";
    public static final String OVERCONSUMPTION_QUEUE = "overconsumption.queue";

    @Bean
    public TopicExchange overconsumptionExchange() {
        return new TopicExchange(OVERCONSUMPTION_EXCHANGE, true, false);
    }

    @Bean
    public Queue overconsumptionQueue() {
        return new Queue(OVERCONSUMPTION_QUEUE, true);
    }

    @Bean
    public Binding overconsumptionBinding(Queue overconsumptionQueue, TopicExchange overconsumptionExchange) {
        return BindingBuilder.bind(overconsumptionQueue)
                .to(overconsumptionExchange)
                .with(OVERCONSUMPTION_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
