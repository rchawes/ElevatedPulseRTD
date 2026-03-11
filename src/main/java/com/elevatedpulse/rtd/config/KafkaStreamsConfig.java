package com.elevatedpulse.rtd.config;

import com.elevatedpulse.rtd.model.CpuMetricEvent;
import com.elevatedpulse.rtd.model.MetricAggregate;
import com.elevatedpulse.rtd.model.PageViewEvent;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Duration;

@Configuration
@EnableKafkaStreams
public class KafkaStreamsConfig {

    private static class CpuAggregate {
        double sum;
        long count;

        public CpuAggregate() {
            this.sum = 0.0;
            this.count = 0;
        }

        public CpuAggregate(double sum, long count) {
            this.sum = sum;
            this.count = count;
        }

        public double getAverage() {
            return count == 0 ? 0.0 : sum / count;
        }
    }

    @Bean
    public KStream<String, PageViewEvent> pageViewStream(StreamsBuilder builder) {
        JsonSerde<PageViewEvent> pageViewSerde = new JsonSerde<>(PageViewEvent.class);
        JsonSerde<MetricAggregate> aggregateSerde = new JsonSerde<>(MetricAggregate.class);

        KStream<String, PageViewEvent> stream = builder.stream("page-views",
                Consumed.with(Serdes.String(), pageViewSerde));

        stream
                .groupBy((key, event) -> event.getUrl(),
                        Grouped.with(Serdes.String(), pageViewSerde))
                .windowedBy(TimeWindows.ofSizeAndGrace(Duration.ofMinutes(1), Duration.ofSeconds(10)))
                .count()
                .toStream()
                .map((windowedKey, count) -> {
                    MetricAggregate aggregate = new MetricAggregate(
                            "page_views",
                            windowedKey.key(),
                            count.doubleValue(),
                            windowedKey.window().startTime(),
                            windowedKey.window().endTime()
                    );
                    return KeyValue.pair(windowedKey.key(), aggregate);
                })
                .to("metric-aggregates",
                        Produced.with(Serdes.String(), aggregateSerde));

        return stream;
    }

    @Bean
    public KStream<String, CpuMetricEvent> cpuMetricStream(StreamsBuilder builder) {
        JsonSerde<CpuMetricEvent> cpuSerde = new JsonSerde<>(CpuMetricEvent.class);
        JsonSerde<MetricAggregate> aggregateSerde = new JsonSerde<>(MetricAggregate.class);
        JsonSerde<CpuAggregate> cpuAggSerde = new JsonSerde<>(CpuAggregate.class);

        KStream<String, CpuMetricEvent> stream = builder.stream("cpu-metrics",
                Consumed.with(Serdes.String(), cpuSerde));

        stream
                .groupBy((key, event) -> event.getHost(),
                        Grouped.with(Serdes.String(), cpuSerde))
                .windowedBy(TimeWindows.ofSizeAndGrace(Duration.ofMinutes(1), Duration.ofSeconds(10)))
                .aggregate(
                        CpuAggregate::new,
                        (key, event, aggregate) -> new CpuAggregate(
                                aggregate.sum + event.getValue(),
                                aggregate.count + 1
                        ),
                        Materialized.with(Serdes.String(), cpuAggSerde)
                )
                .toStream()
                .map((windowedKey, agg) -> {
                    MetricAggregate aggregate = new MetricAggregate(
                            "cpu_usage",
                            windowedKey.key(),
                            agg.getAverage(),
                            windowedKey.window().startTime(),
                            windowedKey.window().endTime()
                    );
                    return KeyValue.pair(windowedKey.key(), aggregate);
                })
                .to("metric-aggregates",
                        Produced.with(Serdes.String(), aggregateSerde));

        return stream;
    }
}