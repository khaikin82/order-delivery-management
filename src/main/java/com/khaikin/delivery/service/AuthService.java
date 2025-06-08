package com.khaikin.delivery.service;

import com.khaikin.delivery.dto.auth.RegisterRequest;
import com.khaikin.delivery.entity.User;
import com.khaikin.delivery.entity.enums.Role;

public interface AuthService {
    User register(RegisterRequest request);
    User authenticate(String username, String rawPassword);
    void changePassword(String username, String oldPassword, String newPassword);
}
