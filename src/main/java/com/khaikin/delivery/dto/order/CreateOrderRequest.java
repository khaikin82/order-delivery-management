package com.khaikin.delivery.dto.order;

import lombok.Data;

@Data
public class CreateOrderRequest {
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;

    private String pickupAddress;
    private String deliveryAddress;

    private String description;
    private double weight;
    private String size;
    private String note;
}
