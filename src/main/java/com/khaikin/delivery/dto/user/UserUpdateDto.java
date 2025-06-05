package com.khaikin.delivery.dto.user;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserUpdateDto {
    @Email
    private String email;

//    @Pattern(regexp = "\\d{10}")
    private String phone;
}
