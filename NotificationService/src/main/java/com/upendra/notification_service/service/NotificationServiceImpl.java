package com.upendra.notification_service.service;

import com.upendra.notification_service.dto.OrderPlaceEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService{

    private final EmailService emailService;
    private final TemplateEmailRenderer templateEmailRenderer;

    public NotificationServiceImpl(EmailService emailService, TemplateEmailRenderer templateEmailRenderer) {
        this.emailService = emailService;
        this.templateEmailRenderer = templateEmailRenderer;
    }

    @Override
    public void sendOrderPlaceNotification(String orderId, String userEmail) {
        try {
            String mailBody = templateEmailRenderer.renderOrderEmail(new OrderPlaceEvent(orderId, userEmail));
            emailService.sendMail(userEmail, "Order Placed", mailBody);
            log.info("Order {} email sent.", orderId);
        } catch (Exception e) {
            log.error("Exception occurred while sending email, order {}, ex: {}", orderId, e.getMessage(), e);
        }
    }
}
