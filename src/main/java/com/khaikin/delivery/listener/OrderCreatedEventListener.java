package com.khaikin.delivery.listener;

import com.khaikin.delivery.event.OrderCreatedEvent;
import com.khaikin.delivery.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedEventListener {
    private final EmailService emailService;

    @EventListener
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        emailService.sendOrderConfirmationEmail(event.getCustomerEmail(), event.getOrderId());
    }
}
