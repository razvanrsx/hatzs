package com.example.monitoring.service;

import com.example.monitoring.config.MessagingConfig;
import com.example.monitoring.model.DeviceMeasurement;
import com.example.monitoring.model.HourlyConsumption;
import com.example.monitoring.model.OverconsumptionAlert;
import com.example.monitoring.repository.HourlyConsumptionRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class ConsumptionService {

    private final HourlyConsumptionRepository repository;
    private final RabbitTemplate rabbitTemplate;
    private final BigDecimal overconsumptionThreshold;

    public ConsumptionService(HourlyConsumptionRepository repository,
                              RabbitTemplate rabbitTemplate,
                              @Value("${monitoring.overconsumption-threshold:100}") BigDecimal overconsumptionThreshold) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
        this.overconsumptionThreshold = overconsumptionThreshold;
    }

    @Transactional
    public HourlyConsumption recordMeasurement(DeviceMeasurement measurement) {
        if (measurement == null) {
            throw new IllegalArgumentException("Measurement must not be null");
        }
        if (measurement.getDeviceId() == null ||
                measurement.getTimestamp() == null ||
                measurement.getConsumption() == null) {
            throw new IllegalArgumentException("Measurement missing required fields");
        }

        // convertim LONG epoch millis -> LocalDateTime
        LocalDateTime hourStart =
                Instant.ofEpochMilli(measurement.getTimestamp())
                        .atZone(ZoneId.systemDefault())
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0)
                        .toLocalDateTime();

        BigDecimal value = measurement.getConsumption();

        HourlyConsumption existing = repository
                .findByDeviceIdAndHourStart(measurement.getDeviceId(), hourStart)
                .orElseGet(() -> {
                    HourlyConsumption hc = new HourlyConsumption();
                    hc.setDeviceId(measurement.getDeviceId());
                    hc.setHourStart(hourStart);
                    hc.setConsumption(BigDecimal.ZERO);
                    return hc;
                });

        BigDecimal previousConsumption = existing.getConsumption();
        BigDecimal updatedConsumption = previousConsumption.add(value);
        existing.setConsumption(updatedConsumption);

        if (previousConsumption.compareTo(overconsumptionThreshold) < 0
                && updatedConsumption.compareTo(overconsumptionThreshold) >= 0) {
            OverconsumptionAlert alert = new OverconsumptionAlert(
                    measurement.getDeviceId(),
                    hourStart,
                    updatedConsumption,
                    overconsumptionThreshold
            );
            rabbitTemplate.convertAndSend(
                    MessagingConfig.OVERCONSUMPTION_EXCHANGE,
                    MessagingConfig.OVERCONSUMPTION_ROUTING_KEY,
                    alert
            );
        }

        return repository.save(existing);
    }

    public List<HourlyConsumption> findByDevice(Long deviceId) {
        return repository.findByDeviceIdOrderByHourStartDesc(deviceId);
    }

    public List<HourlyConsumption> findAll() {
        return repository.findAll();
    }
}
