package com.khaikin.delivery.service;

import com.khaikin.delivery.dto.OrderTrackingHistoryResponse;

import java.util.List;

public interface TrackingService {
    List<OrderTrackingHistoryResponse> getTrackingHistory(String orderCode);
}
