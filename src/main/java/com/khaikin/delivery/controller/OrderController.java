package com.khaikin.delivery.controller;

import com.khaikin.delivery.dto.order.CreateOrderRequest;
import com.khaikin.delivery.dto.order.OrderResponse;
import com.khaikin.delivery.entity.enums.OrderStatus;
import com.khaikin.delivery.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {
    private final OrderService orderService;

    // Lấy danh sách tất cả đơn hàng
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        OrderResponse order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<OrderResponse>> getMyOrders(Authentication authentication) {
        String username = authentication.getName();
        List<OrderResponse> orders = orderService.getMyOrders(username);
        return ResponseEntity.ok(orders);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderResponse> createOrder(
            @RequestBody CreateOrderRequest request,
            BindingResult bindingResult,
            Authentication authentication) {

        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException("Invalid input: " + errors);
        }

        String username = authentication.getName();
        OrderResponse created = orderService.createOrder(request, username);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderResponse> updateOrder(
            @PathVariable Long id,
            @RequestBody CreateOrderRequest request,
            BindingResult bindingResult,
            Authentication authentication) {

        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException("Invalid input: " + errors);
        }

        String username = authentication.getName();
        OrderResponse updated = orderService.updateOrder(id, request, username);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{id}/assign")
    public ResponseEntity<OrderResponse> assignOrder(
            @PathVariable Long id,
            @RequestParam String staffUsername) {
        OrderResponse assigned = orderService.assignOrder(id, staffUsername);
        return ResponseEntity.ok(assigned);
    }

    @PostMapping("/{id}/status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status,
            Authentication authentication) {
        String username = authentication.getName();
        OrderResponse updated = orderService.updateStatus(id, status, username);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrderById(id);
        return ResponseEntity.noContent().build();
    }
}
