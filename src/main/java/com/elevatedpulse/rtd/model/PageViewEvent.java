package com.elevatedpulse.rtd.model;

import java.time.Instant;

public class PageViewEvent {
    private String userId;
    private String url;
    private Instant timestamp;

    public PageViewEvent() {}

    public PageViewEvent(String userId, String url, Instant timestamp) {
        this.userId = userId;
        this.url = url;
        this.timestamp = timestamp;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}