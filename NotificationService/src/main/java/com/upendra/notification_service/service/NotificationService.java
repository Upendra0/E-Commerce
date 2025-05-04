package com.upendra.notification_service.service;

public interface NotificationService {

    void sendOrderPlaceNotification(String orderId, String userEmail);
}
