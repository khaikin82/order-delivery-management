package com.khaikin.delivery.service;

import com.khaikin.delivery.dto.order.CreateOrderRequest;
import com.khaikin.delivery.dto.order.OrderResponse;
import com.khaikin.delivery.entity.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;


public interface OrderService {
//    Page<OrderResponse> getAllOrders(Pageable pageable);
    Page<OrderResponse> getAllOrders(
            Pageable pageable,
            OrderStatus status,
            Boolean hasDeliveryStaff,
            LocalDate dateFrom,
            LocalDate dateTo
    );
//    Page<OrderResponse> getMyOrders(String username, Pageable pageable);
    Page<OrderResponse> getMyOrders(
            String username,
            String orderCode,
            OrderStatus status,
            LocalDate dateFrom,
            LocalDate dateTo,
            Pageable pageable
    );
    Page<OrderResponse> getMyStaffOrders(String staffUsername, Pageable pageable);
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

