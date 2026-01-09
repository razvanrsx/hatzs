package com.example.simulator.producer;

import com.example.simulator.config.MessagingConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class MeasurementProducer {

    private final RabbitTemplate rabbitTemplate;

    public MeasurementProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMeasurement(MeasurementEvent event) {
        rabbitTemplate.convertAndSend(
                MessagingConfig.EXCHANGE,
                MessagingConfig.ROUTING_KEY,
                event
        );
        System.out.println("[SIMULATOR] Sent event: " + event);
    }
}
