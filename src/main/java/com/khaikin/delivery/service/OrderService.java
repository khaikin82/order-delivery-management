package com.khaikin.delivery.service;

import com.khaikin.delivery.dto.CreateOrderRequest;
import com.khaikin.delivery.dto.OrderResponse;
import com.khaikin.delivery.entity.enums.OrderStatus;

import java.util.List;


public interface OrderService {
    List<OrderResponse> getAllOrders();
    List<OrderResponse> getMyOrders(String username);
    OrderResponse getOrderById(Long id);

    OrderResponse createOrder(CreateOrderRequest request, String username);
    OrderResponse assignOrder(Long orderId, String staffUsername);

    OrderResponse updateOrder(Long orderId, CreateOrderRequest request, String username);
    OrderResponse updateStatus(Long orderId, OrderStatus newStatus, String username);

    void deleteOrderById(Long id);
}

