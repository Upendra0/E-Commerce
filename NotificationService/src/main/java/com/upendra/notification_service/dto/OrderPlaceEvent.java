package com.upendra.notification_service.dto;

public record OrderPlaceEvent(String orderId, String email) {
}
