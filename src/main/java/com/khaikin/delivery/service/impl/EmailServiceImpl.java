package com.khaikin.delivery.service.impl;

import com.khaikin.delivery.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Override
    public void sendOrderConfirmationEmail(String toEmail, String orderId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Xác nhận đơn hàng " + orderId);
        message.setText("Cảm ơn bạn đã đặt hàng. Mã đơn hàng của bạn là " + orderId + ".\nChúng tôi sẽ liên hệ sớm nhất có thể.");
        mailSender.send(message);
    }
}
