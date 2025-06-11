package com.khaikin.delivery.controller;

import com.khaikin.delivery.dto.order.AssignStaffRequest;
import com.khaikin.delivery.dto.order.CreateOrderRequest;
import com.khaikin.delivery.dto.order.OrderResponse;
import com.khaikin.delivery.dto.tracking.OrderTrackingHistoryResponse;
import com.khaikin.delivery.entity.enums.OrderStatus;
import com.khaikin.delivery.service.OrderService;
import com.khaikin.delivery.service.TrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {
    private final OrderService orderService;
    private final TrackingService trackingService;

//    @GetMapping
//    public ResponseEntity<Page<OrderResponse>> getAllOrders(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size
//    ) {
//        Pageable pageable = PageRequest.of(page, size);
//        Page<OrderResponse> orderPage = orderService.getAllOrders(pageable);
//        return ResponseEntity.ok(orderPage);
//    }

    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) Boolean hasDeliveryStaff,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderResponse> orderPage = orderService.getAllOrders(
                pageable, status, hasDeliveryStaff, dateFrom, dateTo
        );
        return ResponseEntity.ok(orderPage);
    }


    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        OrderResponse order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/code/{orderCode}")
    public ResponseEntity<OrderResponse> getOrderByCode(@PathVariable String orderCode) {
        OrderResponse order = orderService.getOrderByCode(orderCode);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{orderCode}/tracking")
    public ResponseEntity<List<OrderTrackingHistoryResponse>> getTrackingInfo(@PathVariable String orderCode) {
        List<OrderTrackingHistoryResponse> history = trackingService.getTrackingHistory(orderCode);
        return ResponseEntity.ok(history);
    }

//    @GetMapping("/my")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<Page<OrderResponse>> getMyOrders(
//            Authentication authentication,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size
//    ) {
//        String username = authentication.getName();
//        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending()); // bạn có thể chỉnh sort theo field mong muốn
//
//        Page<OrderResponse> orders = orderService.getMyOrders(username, pageable);
//
//        return ResponseEntity.ok(orders);
//    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<OrderResponse>> getMyOrders(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String orderCode,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo
    ) {
        String username = authentication.getName();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<OrderResponse> orders = orderService.getMyOrders(username, orderCode, status, dateFrom, dateTo, pageable);

        return ResponseEntity.ok(orders);
    }



    @GetMapping("/staff/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<OrderResponse>> getMyStaffOrders(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        String username = authentication.getName();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<OrderResponse> orders = orderService.getMyStaffOrders(username, pageable);
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

    @PutMapping("/by-id/{id}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponse> assignOrder(
            @PathVariable Long id,
            @RequestBody AssignStaffRequest request) {
        OrderResponse assigned = orderService.assignOrder(id, request.getStaffUsername());
        return ResponseEntity.ok(assigned);
    }

    @PutMapping("/by-code/{orderCode}/assign")
//    @PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderResponse> assignOrder(
            @PathVariable String orderCode,
            @RequestBody AssignStaffRequest request) {
        OrderResponse assigned = orderService.assignOrder(orderCode, request.getStaffUsername());
        return ResponseEntity.ok(assigned);
    }

    @PutMapping("/by-id/{id}/status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status,
            Authentication authentication) {
        String username = authentication.getName();
        OrderResponse updated = orderService.updateStatus(id, status, username);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/by-code/{orderCode}/status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable String orderCode,
            @RequestParam OrderStatus status,
            Authentication authentication) {
        String username = authentication.getName();
        OrderResponse updated = orderService.updateStatus(orderCode, status, username);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrderById(id);
        return ResponseEntity.noContent().build();
    }
}
