package com.upendra.notification_service.kafka_listener;

import com.upendra.notification_service.dto.OrderPlaceEvent;
import com.upendra.notification_service.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderPlaceEventListener {

    private final NotificationService notificationService;

    public OrderPlaceEventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "order-placed-v1")
    public void listen(OrderPlaceEvent orderPlaceEvent){
        log.info("Order Placed Event received for Id: {}", orderPlaceEvent.orderId());
        notificationService.sendOrderPlaceNotification(orderPlaceEvent.orderId(), orderPlaceEvent.email());
    }
}
