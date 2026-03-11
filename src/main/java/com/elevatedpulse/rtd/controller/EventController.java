package com.elevatedpulse.rtd.controller;

import com.elevatedpulse.rtd.model.CpuMetricEvent;
import com.elevatedpulse.rtd.model.PageViewEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final long SEND_TIMEOUT_MS = 5000; // 5 second timeout

    public EventController(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/pageview")
    public ResponseEntity<String> postPageView(@RequestBody PageViewEvent event) {
        event.setTimestamp(Instant.now());

        try {
            // Wait for send confirmation with timeout
            kafkaTemplate.send("page-views", event.getUrl(), event)
                    .get(SEND_TIMEOUT_MS, TimeUnit.MILLISECONDS);

            return ResponseEntity.ok("Page view event sent successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send page view: " + e.getMessage());
        }
    }

    @PostMapping("/cpu")
    public ResponseEntity<String> postCpuMetric(@RequestBody CpuMetricEvent event) {
        event.setTimestamp(Instant.now());

        try {
            // Wait for send confirmation with timeout
            kafkaTemplate.send("cpu-metrics", event.getHost(), event)
                    .get(SEND_TIMEOUT_MS, TimeUnit.MILLISECONDS);

            return ResponseEntity.ok("CPU metric sent successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send CPU metric: " + e.getMessage());
        }
    }
}