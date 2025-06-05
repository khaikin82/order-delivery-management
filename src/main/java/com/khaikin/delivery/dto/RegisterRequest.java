package com.khaikin.delivery.dto;

import com.khaikin.delivery.entity.enums.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private Role role;
}
