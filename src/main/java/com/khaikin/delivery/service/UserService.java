package com.khaikin.delivery.service;


import com.khaikin.delivery.dto.UserDto;
import com.khaikin.delivery.dto.UserUpdateDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();
    UserDto getUserById(Long userId);
    UserDto updateUser(Long userId, UserUpdateDto userUpdateDto);
    void deleteUserById(Long userId);
}
