package com.elevatedpulse.rtd.model;

import java.time.Instant;

public class CpuMetricEvent {
    private String host;
    private double value;
    private Instant timestamp;

    public CpuMetricEvent() {}

    public CpuMetricEvent(String host, double value, Instant timestamp) {
        this.host = host;
        this.value = value;
        this.timestamp = timestamp;
    }

    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}