package com.khaikin.delivery.service.impl;

import com.khaikin.delivery.entity.enums.Role;
import jakarta.transaction.Transactional;
import com.khaikin.delivery.dto.user.UserDto;
import com.khaikin.delivery.dto.user.UserUpdateDto;
import com.khaikin.delivery.entity.User;
import com.khaikin.delivery.exception.ResourceNotFoundException;
import com.khaikin.delivery.repository.UserRepository;
import com.khaikin.delivery.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return modelMapper.map(user, UserDto.class);
    }

    @Transactional
    @Override
    public UserDto updateUser(Long userId, UserUpdateDto userUpdateDto) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        existingUser.setEmail(userUpdateDto.getEmail());
        existingUser.setPhone(userUpdateDto.getPhone());
        User savedUser = userRepository.save(existingUser);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public void deleteUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        userRepository.delete(user);
    }

    @Override
    public List<UserDto> getAllUsersByRole(Role role) {
        return userRepository.findAllByRole(role).stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getAllStaffs() {
        return getAllUsersByRole(Role.DELIVERY_STAFF);
    }

}
