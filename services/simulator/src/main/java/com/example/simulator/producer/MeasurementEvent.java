package com.example.simulator.producer;

import java.io.Serializable;

public class MeasurementEvent implements Serializable {

    private Long deviceId;
    private long timestamp;
    private double consumption;

    public MeasurementEvent() {
    }

    public MeasurementEvent(Long deviceId, long timestamp, double consumption) {
        this.deviceId = deviceId;
        this.timestamp = timestamp;
        this.consumption = consumption;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getConsumption() {
        return consumption;
    }

    public void setConsumption(double consumption) {
        this.consumption = consumption;
    }

    @Override
    public String toString() {
        return "MeasurementEvent{" +
                "deviceId=" + deviceId +
                ", timestamp=" + timestamp +
                ", consumption=" + consumption +
                '}';
    }
}
