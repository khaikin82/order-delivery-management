package com.khaikin.delivery.dto.order;

import com.khaikin.delivery.entity.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderResponse {
    private String orderCode;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;

    private String pickupAddress;
    private String deliveryAddress;

    private String description;
    private double weight;
    private String size;
    private String note;

    private OrderStatus status;
    private LocalDateTime createdAt;

    private String customerUsername;
    private String deliveryStaffUsername;
}
