package com.elevatedpulse.rtd.model;

import java.time.Instant;

public class Alert {
    private String message;
    private String level;
    private Instant timestamp;

    public Alert() {}

    public Alert(String message, String level, Instant timestamp) {
        this.message = message;
        this.level = level;
        this.timestamp = timestamp;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}