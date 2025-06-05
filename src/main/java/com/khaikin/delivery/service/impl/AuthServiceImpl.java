package com.khaikin.delivery.service.impl;

import com.khaikin.delivery.entity.User;
import com.khaikin.delivery.entity.enums.Role;
import com.khaikin.delivery.exception.BadRequestException;
import com.khaikin.delivery.exception.ConflictException;
import com.khaikin.delivery.exception.InvalidCredentialsException;
import com.khaikin.delivery.exception.ResourceNotFoundException;
import com.khaikin.delivery.repository.UserRepository;
import com.khaikin.delivery.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public User register(String username, String password, Role role) {
        if (userRepository.findByUsername(username).isPresent())
            throw new ConflictException("A user with username '" + username + "' already exists.");

        if (role == null) {
            role = Role.CUSTOMER;
        }

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(role)
                .build();
        return userRepository.save(user);
    }

    @Override
    public User authenticate(String username, String rawPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid password");
        }
        return user;
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadRequestException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
