package com.elevatedpulse.rtd.controller;

import com.elevatedpulse.rtd.service.StatsService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class RangeController {

    private final StatsService statsService;

    public RangeController(StatsService statsService) {
        this.statsService = statsService;
    }

    @MessageMapping("/setRange")
    public void setRange(int minutes) {
        statsService.setTimeRangeMinutes(minutes);
    }
}