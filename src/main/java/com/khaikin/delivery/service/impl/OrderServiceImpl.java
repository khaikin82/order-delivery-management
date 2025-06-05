package com.khaikin.delivery.service.impl;

import com.khaikin.delivery.dto.CreateOrderRequest;
import com.khaikin.delivery.dto.OrderResponse;
import com.khaikin.delivery.entity.Order;
import com.khaikin.delivery.entity.User;
import com.khaikin.delivery.entity.enums.OrderStatus;
import com.khaikin.delivery.exception.ConflictException;
import com.khaikin.delivery.exception.ResourceNotFoundException;
import com.khaikin.delivery.repository.OrderRepository;
import com.khaikin.delivery.repository.UserRepository;
import com.khaikin.delivery.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    @Override
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", id));
        return mapToResponse(order);
    }

    @Override
    public List<OrderResponse> getMyOrders(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        List<Order> orders = orderRepository.findByCustomer(user);
        return orders.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse createOrder(CreateOrderRequest request, String username) {
        User customer = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        Order order = modelMapper.map(request, Order.class); // map từ DTO sang Entity

        order.setCustomer(customer);
        order.setOrderCode(generateOrderCode());
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);
        log.info("Order {} created by user {}", savedOrder.getOrderCode(), username);

        return mapToResponse(savedOrder);
    }

    @Override
    public OrderResponse assignOrder(Long orderId, String staffUsername) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", orderId));

        User staff = userRepository.findByUsername(staffUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", "username", staffUsername));

        order.setDeliveryStaff(staff);
        order.setStatus(OrderStatus.ASSIGNED);
        order.setUpdatedAt(LocalDateTime.now());

        Order saved = orderRepository.save(order);
        log.info("Order {} assigned to staff {}", orderId, staffUsername);

        return mapToResponse(saved);
    }

    @Override
    public OrderResponse updateOrder(Long orderId, CreateOrderRequest request, String username) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", orderId));

        // Chỉ cho phép khách hàng tạo đơn sửa đơn
        if (!order.getCustomer().getUsername().equals(username)) {
            throw new ConflictException("You are not allowed to update this order.");
        }

        // Map các trường từ request vào entity (cập nhật dữ liệu)
        modelMapper.map(request, order);
        order.setUpdatedAt(LocalDateTime.now());

        Order updatedOrder = orderRepository.save(order);
        log.info("Order {} updated by user {}", updatedOrder.getOrderCode(), username);

        return mapToResponse(updatedOrder);
    }

    @Override
    public OrderResponse updateStatus(Long orderId, OrderStatus newStatus, String username) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", orderId));

        if (order.getDeliveryStaff() == null || !order.getDeliveryStaff().getUsername().equals(username)) {
            throw new ConflictException("You are not assigned to this order.");
        }

        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());

        Order saved = orderRepository.save(order);
        log.info("Order {} status updated to {} by {}", orderId, newStatus, username);

        return mapToResponse(saved);
    }

    @Override
    public void deleteOrderById(Long id) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", id));
        orderRepository.delete(existingOrder);
        log.info("Order {} deleted", id);
    }



    private String generateOrderCode() {
        return "ORD" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
    }

    private OrderResponse mapToResponse(Order order) {
        OrderResponse response = modelMapper.map(order, OrderResponse.class);

        // bổ sung các trường không tự động map được
        response.setCustomerUsername(order.getCustomer() != null ? order.getCustomer().getUsername() : null);
        response.setDeliveryStaffUsername(order.getDeliveryStaff() != null ? order.getDeliveryStaff().getUsername() : null);

        return response;
    }
}
