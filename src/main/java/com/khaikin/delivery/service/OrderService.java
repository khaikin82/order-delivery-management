package com.khaikin.delivery.service;

import com.khaikin.delivery.dto.order.CreateOrderRequest;
import com.khaikin.delivery.dto.order.OrderResponse;
import com.khaikin.delivery.entity.enums.OrderStatus;

import java.util.List;


public interface OrderService {
    List<OrderResponse> getAllOrders();
    List<OrderResponse> getMyOrders(String username);
    List<OrderResponse> getMyStaffOrders(String staffUsername);
    OrderResponse getOrderById(Long id);
    OrderResponse getOrderByCode(String orderCode);

    OrderResponse createOrder(CreateOrderRequest request, String username);
    OrderResponse assignOrder(Long orderId, String staffUsername);
    OrderResponse assignOrder(String orderCode, String staffUsername);

    OrderResponse updateOrder(Long orderId, CreateOrderRequest request, String username);
    OrderResponse updateStatus(Long orderId, OrderStatus newStatus, String username);
    OrderResponse updateStatus(String orderCode, OrderStatus newStatus, String username);

    void deleteOrderById(Long id);
}

