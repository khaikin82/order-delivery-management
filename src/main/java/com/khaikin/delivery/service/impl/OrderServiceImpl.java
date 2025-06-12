package com.khaikin.delivery.service.impl;

import com.khaikin.delivery.dto.order.CreateOrderRequest;
import com.khaikin.delivery.dto.order.OrderResponse;
import com.khaikin.delivery.dto.tracking.OrderTrackingHistoryResponse;
import com.khaikin.delivery.entity.Order;
import com.khaikin.delivery.entity.OrderTrackingHistory;
import com.khaikin.delivery.entity.User;
import com.khaikin.delivery.entity.enums.OrderStatus;
import com.khaikin.delivery.event.OrderCreatedEvent;
import com.khaikin.delivery.event.OrderStatusChangedEvent;
import com.khaikin.delivery.exception.ConflictException;
import com.khaikin.delivery.exception.ResourceNotFoundException;
import com.khaikin.delivery.repository.OrderRepository;
import com.khaikin.delivery.repository.OrderTrackingHistoryRepository;
import com.khaikin.delivery.repository.UserRepository;
import com.khaikin.delivery.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final OrderTrackingHistoryRepository historyRepository;


//    @Override
//    public Page<OrderResponse> getAllOrders(Pageable pageable) {
//        System.out.println(orderRepository.findAll(pageable));
//        return orderRepository.findAll(pageable)
//                .map(this::mapToResponse);
//    }
    @Override
    public Page<OrderResponse> getAllOrders(
            Pageable pageable,
            OrderStatus status,
            Boolean hasDeliveryStaff,
            LocalDate dateFrom,
            LocalDate dateTo
    ) {
        Specification<Order> spec = (root, query, cb) -> cb.conjunction(); // tương đương với true predicate

        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }

        if (hasDeliveryStaff != null) {
            if (hasDeliveryStaff) {
                spec = spec.and((root, query, cb) -> cb.isNotNull(root.get("deliveryStaff")));
            } else {
                spec = spec.and((root, query, cb) -> cb.isNull(root.get("deliveryStaff")));
            }
        }

        if (dateFrom != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createdAt"), dateFrom.atStartOfDay()));
        }

        if (dateTo != null) {
            spec = spec.and((root, query, cb) -> cb.lessThan(root.get("createdAt"), dateTo.plusDays(1).atStartOfDay()));
        }

        Page<Order> orderPage = orderRepository.findAll(spec, pageable);
        return orderPage.map(this::mapToResponse);
    }

    @Override
    @Cacheable(value = "orders", key = "#id")
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", id));
        return mapToResponse(order);
    }

    @Override
    @Cacheable(value = "orders", key = "#orderCode")
    public OrderResponse getOrderByCode(String orderCode) {
        Order order = orderRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderCode", orderCode));
        return mapToResponse(order);
    }


//    @Override
//    public Page<OrderResponse> getMyOrders(String username, Pageable pageable) {
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
//        Page<Order> orders = orderRepository.findByCustomer(user, pageable);
//        return orders.map(this::mapToResponse);
//    }

    @Override
    public Page<OrderResponse> getMyOrders(
            String username,
            String orderCode,
            OrderStatus status,
            LocalDate dateFrom,
            LocalDate dateTo,
            Pageable pageable
    ) {
        Specification<Order> spec = (root, query, cb) -> cb.conjunction();

        // Lọc theo username của customer
        spec = spec.and((root, query, cb) ->
                                cb.equal(root.get("customer").get("username"), username)
        );

        // Lọc theo mã đơn hàng
        if (orderCode != null && !orderCode.isBlank()) {
            spec = spec.and((root, query, cb) ->
                                    cb.like(cb.lower(root.get("orderCode")), "%" + orderCode.toLowerCase() + "%")
            );
        }

        // Lọc theo trạng thái
        if (status != null) {
            try {
                spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
            } catch (IllegalArgumentException e) {
                // Có thể throw custom exception nếu cần
            }
        }

        // Lọc theo khoảng ngày tạo
        if (dateFrom != null) {
            spec = spec.and((root, query, cb) ->
                                    cb.greaterThanOrEqualTo(root.get("createdAt"), dateFrom.atStartOfDay())
            );
        }

        if (dateTo != null) {
            spec = spec.and((root, query, cb) ->
                                    cb.lessThan(root.get("createdAt"), dateTo.plusDays(1).atStartOfDay())
            );
        }

        Page<Order> orderPage = orderRepository.findAll(spec, pageable);
        return orderPage.map(this::mapToResponse);
    }

    @Override
    public Page<OrderResponse> getMyStaffOrders(String staffUsername, Pageable pageable) {
        User user = userRepository.findByUsername(staffUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", "username", staffUsername));
        Page<Order> ordersPage = orderRepository.findByDeliveryStaff(user, pageable);
        return ordersPage.map(this::mapToResponse);
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
        eventPublisher.publishEvent(new OrderCreatedEvent(order.getOrderCode(), customer.getEmail()));
        eventPublisher.publishEvent(new OrderStatusChangedEvent(
                this, savedOrder.getId(), null, savedOrder.getStatus(), username
        ));

        return mapToResponse(savedOrder);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "orders", allEntries = true)
    })
    public OrderResponse assignOrder(Long orderId, String staffUsername, String adminUsername) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", orderId));

        User staff = userRepository.findByUsername(staffUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", "username", staffUsername));

        OrderStatus oldStatus = order.getStatus();

        order.setDeliveryStaff(staff);
        if (order.getStatus() == OrderStatus.CREATED) {
            order.setStatus(OrderStatus.ASSIGNED);
        }
        order.setUpdatedAt(LocalDateTime.now());

        Order saved = orderRepository.save(order);
        log.info("Order with orderId {} assigned to staff {}", orderId, staffUsername);

        eventPublisher.publishEvent(new OrderStatusChangedEvent(
                this, saved.getId(), oldStatus, saved.getStatus(), adminUsername
        ));

        return mapToResponse(saved);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "orders", allEntries = true)
    })
    public OrderResponse assignOrder(String orderCode, String staffUsername, String adminUsername) {
        Order order = orderRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderCode", orderCode));

        User staff = userRepository.findByUsername(staffUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", "username", staffUsername));

        OrderStatus oldStatus = order.getStatus();

        order.setDeliveryStaff(staff);
        if (order.getStatus() == OrderStatus.CREATED) {
            order.setStatus(OrderStatus.ASSIGNED);
        }
        order.setUpdatedAt(LocalDateTime.now());

        Order saved = orderRepository.save(order);
        log.info("Order with orderCode {} assigned to staff {}", orderCode, staffUsername);

        eventPublisher.publishEvent(new OrderStatusChangedEvent(
                this, saved.getId(), oldStatus, saved.getStatus(), adminUsername
        ));

        return mapToResponse(saved);
    }

    @Transactional
    @Override
    @Caching(evict = {
            @CacheEvict(value = "orders", allEntries = true)
    })
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

    @Transactional
    @Override
    @Caching(evict = {
            @CacheEvict(value = "orders", allEntries = true)
    })
    public OrderResponse updateStatus(Long orderId, OrderStatus newStatus, String username) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", orderId));

        if (order.getDeliveryStaff() == null || !order.getDeliveryStaff().getUsername().equals(username)) {
            throw new AuthorizationDeniedException("You are not assigned to this order.");
        }

        OrderStatus oldStatus = order.getStatus();

        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());
        Order saved = orderRepository.save(order);

        // Publish event
        eventPublisher.publishEvent(new OrderStatusChangedEvent(
                this, orderId, oldStatus, newStatus, username
        ));

        log.info("Order with orderId {} status updated to {} by {}", orderId, newStatus, username);

        return mapToResponse(saved);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "orders", allEntries = true)
    })
    public OrderResponse updateStatus(String orderCode, OrderStatus newStatus, String username) {
        Order order = orderRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderCode", orderCode));

        if (order.getDeliveryStaff() == null || !order.getDeliveryStaff().getUsername().equals(username)) {
            throw new AuthorizationDeniedException("You are not assigned to this order.");
        }

        OrderStatus oldStatus = order.getStatus();

        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());
        Order saved = orderRepository.save(order);

        // Publish event
        eventPublisher.publishEvent(new OrderStatusChangedEvent(
                this, order.getId(), oldStatus, newStatus, username
        ));

        log.info("Order with orderCode {} status updated to {} by {}", orderCode, newStatus, username);

        return mapToResponse(saved);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "orders", allEntries = true)
    })
    public void deleteOrderById(Long id) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", id));
        orderRepository.delete(existingOrder);
        log.info("Order {} deleted", id);
    }


    private String generateOrderCode() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // Đếm số order hôm nay → bạn cần query DB, ở đây tạm giả lập:
        long countToday = orderRepository.countByCreatedAtBetween(
                LocalDate.now().atStartOfDay(),
                LocalDate.now().plusDays(1).atStartOfDay()
        );

        long nextOrderNumber = countToday + 1;
        String orderNumberPart = String.format("%04d", nextOrderNumber); // padding 3 số: 001, 002,...
        String randomPart = generateRandomAlphaNumeric(4); // 4 ký tự random

        return "ORD" + datePart + orderNumberPart + randomPart;
    }

    private String generateRandomAlphaNumeric(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new SecureRandom(); // dùng SecureRandom cho an toàn hơn

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }

        return sb.toString();
    }


    private OrderResponse mapToResponse(Order order) {
        OrderResponse response = modelMapper.map(order, OrderResponse.class);

        // bổ sung các trường không tự động map được
        response.setCustomerUsername(order.getCustomer() != null ? order.getCustomer().getUsername() : null);
        response.setDeliveryStaffUsername(order.getDeliveryStaff() != null ? order.getDeliveryStaff().getUsername() : null);

        List<OrderTrackingHistory> history =
                historyRepository.findByOrderOrderCodeOrderByChangedAtAsc(order.getOrderCode());

        List<OrderTrackingHistoryResponse> historyResponse = history.stream()
                .map(h -> modelMapper.map(h, OrderTrackingHistoryResponse.class))
                .toList();
        response.setTrackingHistory(historyResponse);
        return response;
    }
}
