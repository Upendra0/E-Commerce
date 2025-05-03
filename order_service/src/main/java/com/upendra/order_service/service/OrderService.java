package com.upendra.order_service.service;

import com.upendra.order_service.client.InventoryServiceClient;
import com.upendra.order_service.dto.*;
import com.upendra.order_service.exception.InventoryException;
import com.upendra.order_service.model.Order;
import com.upendra.order_service.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final InventoryServiceClient inventoryServiceClient;
    private final KafkaTemplate<String, OrderPlacedEvent> orderPlacedEventKafkaTemplate;

    public OrderService(OrderRepository orderRepository, InventoryServiceClient inventoryServiceClient, KafkaTemplate<String, OrderPlacedEvent> orderPlacedEventKafkaTemplate) {
        this.orderRepository = orderRepository;
        this.inventoryServiceClient = inventoryServiceClient;
        this.orderPlacedEventKafkaTemplate = orderPlacedEventKafkaTemplate;
    }

    public OrderResponse placeOrder(OrderRequest orderRequest) {
        log.info("Placing order for skuCode: {}, quantity: {}", orderRequest.skuCode(), orderRequest.quantity());

        BookStockResponse bookStockResponse = inventoryServiceClient.bookStock(new BookStockRequest(orderRequest.skuCode(), orderRequest.quantity()));
        if(bookStockResponse.status()){
            log.info("Stock booked successfully for skuCode: {}", orderRequest.skuCode());
            Order order = new Order(UUID.randomUUID().toString(), orderRequest.skuCode(), orderRequest.quantity(), BigDecimal.valueOf(orderRequest.price()));
            orderRepository.save(order);
            log.info("Order Placed, id: {}", order.getOrderId());

            // send to kafka
            orderPlacedEventKafkaTemplate.send("order-placed-v1", order.getOrderId(), new OrderPlacedEvent(order.getOrderId(), "upendra.kumar06704@gmail.com"));

            return new OrderResponse(order.getOrderId(), order.getSkuCode(), order.getQuantity(), order.getPrice().doubleValue());
        } else{
            log.info("Failed to book stock for skuCode: {}, reason: {}", orderRequest.skuCode(), bookStockResponse.message());
            throw new InventoryException("Failed to book stock for skuCode: " + orderRequest.skuCode() + ", reason: " + bookStockResponse.message());
        }
    }
}
