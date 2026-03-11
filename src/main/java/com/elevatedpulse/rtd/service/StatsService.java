package com.elevatedpulse.rtd.service;

import com.elevatedpulse.rtd.model.MetricAggregate;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatsService {

    private final InfluxDBClient influxDBClient;
    private final SimpMessagingTemplate messagingTemplate;
    private int timeRangeMinutes = 10;

    public StatsService(InfluxDBClient influxDBClient, SimpMessagingTemplate messagingTemplate) {
        this.influxDBClient = influxDBClient;
        this.messagingTemplate = messagingTemplate;
    }

    public void setTimeRangeMinutes(int minutes) {
        this.timeRangeMinutes = minutes;
    }

    @Scheduled(fixedDelay = 5000)
    public void broadcastStats() {
        // ... rest of your code
    }
}