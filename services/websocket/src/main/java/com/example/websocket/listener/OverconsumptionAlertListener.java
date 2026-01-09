package com.example.websocket.listener;

import com.example.websocket.config.MessagingConfig;
import com.example.websocket.handler.NotificationWebSocketHandler;
import com.example.websocket.model.OverconsumptionAlert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OverconsumptionAlertListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(OverconsumptionAlertListener.class);

    private final NotificationWebSocketHandler handler;

    public OverconsumptionAlertListener(NotificationWebSocketHandler handler) {
        this.handler = handler;
    }

    @RabbitListener(queues = MessagingConfig.OVERCONSUMPTION_QUEUE)
    public void receiveAlert(OverconsumptionAlert alert) {
        LOGGER.info("Received overconsumption alert for device {}", alert.getDeviceId());
        handler.broadcast(alert);
    }
}
