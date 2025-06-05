package com.khaikin.delivery.controller;

import com.khaikin.delivery.dto.OrderStatusHistoryDto;
import com.khaikin.delivery.service.TrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tracking")
@RequiredArgsConstructor
public class TrackingController {
    private final TrackingService trackingService;

    @GetMapping("/{orderCode}")
    public ResponseEntity<List<OrderStatusHistoryDto>> getTrackingInfo(@PathVariable String orderCode) {
        List<OrderStatusHistoryDto> history = trackingService.getTrackingHistory(orderCode);
        return ResponseEntity.ok(history);
    }
}
