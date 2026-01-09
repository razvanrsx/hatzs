package com.example.websocket.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OverconsumptionAlert {

    private Long deviceId;
    private LocalDateTime hourStart;
    private BigDecimal consumption;
    private BigDecimal threshold;

    public OverconsumptionAlert() {
    }

    public OverconsumptionAlert(Long deviceId, LocalDateTime hourStart, BigDecimal consumption, BigDecimal threshold) {
        this.deviceId = deviceId;
        this.hourStart = hourStart;
        this.consumption = consumption;
        this.threshold = threshold;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public LocalDateTime getHourStart() {
        return hourStart;
    }

    public void setHourStart(LocalDateTime hourStart) {
        this.hourStart = hourStart;
    }

    public BigDecimal getConsumption() {
        return consumption;
    }

    public void setConsumption(BigDecimal consumption) {
        this.consumption = consumption;
    }

    public BigDecimal getThreshold() {
        return threshold;
    }

    public void setThreshold(BigDecimal threshold) {
        this.threshold = threshold;
    }
}
