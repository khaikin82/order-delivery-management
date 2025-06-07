package com.khaikin.delivery.service;

public interface EmailService {
    void sendOrderConfirmationEmail(String toEmail, String orderId);
}
