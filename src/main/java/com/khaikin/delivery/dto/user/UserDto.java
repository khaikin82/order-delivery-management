package com.khaikin.delivery.dto.user;

import com.khaikin.delivery.entity.enums.Role;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String fullName;
    private String username;
    private String email;
    private String phone;
    private Role role;
}
