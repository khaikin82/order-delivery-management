package com.khaikin.delivery.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserUpdateDto {
    @Email
    private String email;

//    @Pattern(regexp = "\\d{10}")
    private String phone;
}
