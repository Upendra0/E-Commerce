package com.upendra.order_service.kafka_producer_listener;

import com.upendra.order_service.dto.OrderPlacedEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderPlacedEventProducerListener implements ProducerListener<String, OrderPlacedEvent> {

    @Override
    public void onSuccess(ProducerRecord<String, OrderPlacedEvent> producerRecord, RecordMetadata recordMetadata) {
        log.info("Order id: {} written to kafka", producerRecord.value().orderId());
    }

    @Override
    public void onError(ProducerRecord<String, OrderPlacedEvent> producerRecord, RecordMetadata recordMetadata, Exception exception) {
        ProducerListener.super.onError(producerRecord, recordMetadata, exception);
    }
}
