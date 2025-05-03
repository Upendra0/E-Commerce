package com.upendra.order_service.dto;

public record OrderPlacedEvent(String orderId, String email) {
}
