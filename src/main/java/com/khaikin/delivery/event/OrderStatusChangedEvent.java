package com.khaikin.delivery.event;

import com.khaikin.delivery.entity.enums.OrderStatus;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OrderStatusChangedEvent extends ApplicationEvent {

    private final Long orderId;
    private final OrderStatus oldStatus;
    private final OrderStatus newStatus;
    private final String performedBy;

    public OrderStatusChangedEvent(Object source, Long orderId, OrderStatus oldStatus, OrderStatus newStatus, String performedBy) {
        super(source);
        this.orderId = orderId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.performedBy = performedBy;
    }
}