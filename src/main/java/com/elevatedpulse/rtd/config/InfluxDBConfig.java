package com.elevatedpulse.rtd.config;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxDBConfig {

    private final InfluxDBProperties properties;

    public InfluxDBConfig(InfluxDBProperties properties) {
        this.properties = properties;
    }

    @Bean
    public InfluxDBClient influxDBClient() {
        return InfluxDBClientFactory.create(
                properties.getUrl(),
                properties.getToken().toCharArray(),
                properties.getOrg(),
                properties.getBucket()
        );
    }
}