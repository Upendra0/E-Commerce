package com.upendra.notification_service.service;

import com.upendra.notification_service.dto.OrderPlaceEvent;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.util.List;

@Service
public class TemplateEmailRenderer {

    private final TemplateEngine templateEngine;

    public TemplateEmailRenderer(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String renderOrderEmail(OrderPlaceEvent order) {
        Context context = new Context();
        context.setVariable("customerName", "Upendra Kumar");
        context.setVariable("orderId", order.orderId());
        context.setVariable("orderDate", LocalDate.now());
        context.setVariable("items", List.of());
        context.setVariable("total", 500);

        return templateEngine.process("order-details", context);
    }
}

