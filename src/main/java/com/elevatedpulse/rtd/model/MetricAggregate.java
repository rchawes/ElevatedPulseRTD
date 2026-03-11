package com.elevatedpulse.rtd.model;

import java.time.Instant;

public class MetricAggregate {
    private String name;
    private String dimension;
    private double value;
    private Instant windowStart;
    private Instant windowEnd;

    public MetricAggregate() {}

    public MetricAggregate(String name, String dimension, double value, Instant windowStart, Instant windowEnd) {
        this.name = name;
        this.dimension = dimension;
        this.value = value;
        this.windowStart = windowStart;
        this.windowEnd = windowEnd;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDimension() { return dimension; }
    public void setDimension(String dimension) { this.dimension = dimension; }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }

    public Instant getWindowStart() { return windowStart; }
    public void setWindowStart(Instant windowStart) { this.windowStart = windowStart; }

    public Instant getWindowEnd() { return windowEnd; }
    public void setWindowEnd(Instant windowEnd) { this.windowEnd = windowEnd; }
}