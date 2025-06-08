package com.khaikin.delivery.dto.auth;

import com.khaikin.delivery.entity.enums.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String fullName;
    private String email;
    private Role role;
}
