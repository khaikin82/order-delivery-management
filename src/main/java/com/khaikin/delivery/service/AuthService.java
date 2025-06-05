package com.khaikin.delivery.service;

import com.khaikin.delivery.entity.User;
import com.khaikin.delivery.entity.enums.Role;

public interface AuthService {
    User register(String username, String password, Role role);
    User authenticate(String username, String rawPassword);
    void changePassword(String username, String oldPassword, String newPassword);
}
