package com.example.monitoring.service;

import com.example.monitoring.model.DeviceMeasurement;
import com.example.monitoring.model.HourlyConsumption;
import com.example.monitoring.repository.HourlyConsumptionRepository;
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

    public ConsumptionService(HourlyConsumptionRepository repository) {
        this.repository = repository;
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

        existing.setConsumption(existing.getConsumption().add(value));

        return repository.save(existing);
    }

    public List<HourlyConsumption> findByDevice(Long deviceId) {
        return repository.findByDeviceIdOrderByHourStartDesc(deviceId);
    }

    public List<HourlyConsumption> findAll() {
        return repository.findAll();
    }
}
