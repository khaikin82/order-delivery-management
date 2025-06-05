package com.khaikin.delivery.service;

import com.khaikin.delivery.dto.OrderStatusHistoryDto;

import java.util.List;

public interface TrackingService {
    List<OrderStatusHistoryDto> getTrackingHistory(String orderCode);
}
