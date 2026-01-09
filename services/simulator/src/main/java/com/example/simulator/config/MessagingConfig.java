package com.example.simulator.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {

    // ai grijă ca numele să fie la fel ca în monitoring-service
    public static final String EXCHANGE = "measurement.exchange";
    public static final String ROUTING_KEY = "measurement.routing-key";
    public static final String QUEUE = "measurement.queue";

    @Bean
    public TopicExchange measurementExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue measurementQueue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public Binding measurementBinding(Queue measurementQueue, TopicExchange measurementExchange) {
        return BindingBuilder
                .bind(measurementQueue)
                .to(measurementExchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}
