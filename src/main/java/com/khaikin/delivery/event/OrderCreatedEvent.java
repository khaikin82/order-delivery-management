package com.khaikin.delivery.event;

import lombok.Getter;

@Getter
public class OrderCreatedEvent {
    private final String orderId;
    private final String customerEmail;

    public OrderCreatedEvent(String orderId, String customerEmail) {
        this.orderId = orderId;
        this.customerEmail = customerEmail;
    }

}
