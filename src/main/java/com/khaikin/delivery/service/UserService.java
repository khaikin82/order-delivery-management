package com.khaikin.delivery.service;


import com.khaikin.delivery.dto.user.UserDto;
import com.khaikin.delivery.dto.user.UserUpdateDto;
import com.khaikin.delivery.entity.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserDto> getAllUsers(Pageable pageable);
    UserDto getUserById(Long userId);
    UserDto getUserByEmail(String email);
    UserDto updateUser(Long userId, UserUpdateDto userUpdateDto);
    void deleteUserById(Long userId);
    Page<UserDto> getAllUsersByRole(Role role, Pageable pageable);
    Page<UserDto> getAllStaffs(Pageable pageable);
}
