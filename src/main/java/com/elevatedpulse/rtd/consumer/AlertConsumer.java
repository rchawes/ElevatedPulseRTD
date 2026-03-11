package com.elevatedpulse.rtd.consumer;

import com.elevatedpulse.rtd.model.Alert;
import com.elevatedpulse.rtd.model.MetricAggregate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class AlertConsumer {

    private final SimpMessagingTemplate messagingTemplate;

    public AlertConsumer(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @KafkaListener(topics = "metric-aggregates", groupId = "alert-consumer")
    public void checkAlert(MetricAggregate aggregate) {
        if ("cpu_usage".equals(aggregate.getName()) && aggregate.getValue() > 80.0) {
            Alert alert = new Alert(
                    "High CPU on " + aggregate.getDimension() + ": " + String.format("%.1f", aggregate.getValue()) + "%",
                    "critical",
                    Instant.now()
            );
            messagingTemplate.convertAndSend("/topic/alerts", alert);
        }
    }
}