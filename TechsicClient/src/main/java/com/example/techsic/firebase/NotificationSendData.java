package com.example.techsic.firebase;

import java.util.Map;

public class NotificationSendData {
    private String to;
    Map<String,String> notification;

    public NotificationSendData(String to, Map<String, String> notification) {
        this.to = to;
        this.notification = notification;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Map<String, String> getNotification() {
        return notification;
    }

    public void setNotification(Map<String, String> notification) {
        this.notification = notification;
    }
}
