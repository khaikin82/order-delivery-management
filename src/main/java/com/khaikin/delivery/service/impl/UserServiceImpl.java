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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<UserDto> getAllUsers(Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(pageable);
        return usersPage.map(user -> modelMapper.map(user, UserDto.class));
    }


    @Override
    @Cacheable(value = "users", key = "#userId")
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    @Cacheable(value = "usersByEmail", key = "#email")
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return modelMapper.map(user, UserDto.class);
    }

    @Transactional
    @Override
    @Caching(evict = {
            @CacheEvict(value = "users", key = "#userId"),
            @CacheEvict(value = "usersByEmail", allEntries = true)
    })
    public UserDto updateUser(Long userId, UserUpdateDto userUpdateDto) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        existingUser.setEmail(userUpdateDto.getEmail());
        existingUser.setPhone(userUpdateDto.getPhone());
        User savedUser = userRepository.save(existingUser);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Caching(evict = {
            @CacheEvict(value = "users", key = "#userId"),
            @CacheEvict(value = "usersByEmail", allEntries = true)
    })
    @Override
    public void deleteUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        userRepository.delete(user);
    }

    @Override
    public Page<UserDto> getAllUsersByRole(Role role, Pageable pageable) {
        Page<User> usersPage = userRepository.findAllByRole(role, pageable);
        return usersPage.map(user -> modelMapper.map(user, UserDto.class));
    }


    public Page<UserDto> getAllStaffs(Pageable pageable) {
        return getAllUsersByRole(Role.DELIVERY_STAFF, pageable);
    }


}
