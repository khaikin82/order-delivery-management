package com.khaikin.delivery.service;


import com.khaikin.delivery.dto.user.UserDto;
import com.khaikin.delivery.dto.user.UserUpdateDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();
    UserDto getUserById(Long userId);
    UserDto getUserByEmail(String email);
    UserDto updateUser(Long userId, UserUpdateDto userUpdateDto);
    void deleteUserById(Long userId);
}
