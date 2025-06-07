package com.khaikin.delivery.dto;

import com.khaikin.delivery.entity.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderTrackingHistoryResponse {
    private OrderStatus status;
    private LocalDateTime changedAt;
    private String changedBy;
}
