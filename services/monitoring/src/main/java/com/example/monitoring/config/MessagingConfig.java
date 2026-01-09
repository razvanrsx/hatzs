package com.example.monitoring.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {

    public static final String EXCHANGE = "measurement.exchange";
    public static final String ROUTING_KEY = "measurement.routing-key";
    public static final String QUEUE = "measurement.queue";
    public static final String OVERCONSUMPTION_EXCHANGE = "overconsumption.exchange";
    public static final String OVERCONSUMPTION_ROUTING_KEY = "overconsumption.routing-key";
    public static final String OVERCONSUMPTION_QUEUE = "overconsumption.queue";

    // ✔ create exchange
    @Bean
    public TopicExchange measurementExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    // ✔ create queue
    @Bean
    public Queue measurementQueue() {
        return new Queue(QUEUE, true);
    }

    // ✔ bind queue to exchange
    @Bean
    public Binding measurementBinding(Queue measurementQueue, TopicExchange measurementExchange) {
        return BindingBuilder.bind(measurementQueue)
                .to(measurementExchange)
                .with(ROUTING_KEY);
    }

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

    // ✔ JSON converter
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // ✔ RabbitTemplate for sending messages (if needed)
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    // ✔ Rabbit listener container
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter messageConverter) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
    }
}
