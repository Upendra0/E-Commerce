package com.upendra.notification_service.service;

public interface EmailService {

    void sendMail(String to, String subject, String body);
}
