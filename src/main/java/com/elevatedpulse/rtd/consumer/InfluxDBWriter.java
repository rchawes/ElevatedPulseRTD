package com.elevatedpulse.rtd.consumer;

import com.elevatedpulse.rtd.model.MetricAggregate;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class InfluxDBWriter {

    private final InfluxDBClient influxDBClient;

    public InfluxDBWriter(InfluxDBClient influxDBClient) {
        this.influxDBClient = influxDBClient;
    }

    @KafkaListener(topics = "metric-aggregates", groupId = "influx-writer")
    public void write(MetricAggregate aggregate) {
        WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();

        Point point = Point.measurement(aggregate.getName())
                .addTag("dimension", aggregate.getDimension())
                .addField("value", aggregate.getValue())
                .time(aggregate.getWindowStart(), WritePrecision.MS);

        writeApi.writePoint(point);
    }
}