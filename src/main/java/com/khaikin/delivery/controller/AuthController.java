package com.khaikin.delivery.controller;

import com.khaikin.delivery.config.JwtService;
import com.khaikin.delivery.dto.auth.AuthRequest;
import com.khaikin.delivery.dto.auth.AuthResponse;
import com.khaikin.delivery.dto.auth.ChangePasswordRequest;
import com.khaikin.delivery.dto.auth.RegisterRequest;
import com.khaikin.delivery.entity.User;
import com.khaikin.delivery.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtService jwtService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        User user = authService.register(request);
        String token = jwtService.generateToken(user);
        return new ResponseEntity<>(new AuthResponse(user.getUsername(), token), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        User user = authService.authenticate(request.getUsername(), request.getPassword());
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(user.getUsername(), token));
    }

    @PutMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request, Authentication authentication) {
        String username = authentication.getName();
        authService.changePassword(username, request.getOldPassword(), request.getNewPassword());
        return ResponseEntity.ok("Password changed successfully! " + username);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        // Logout chỉ đơn giản là client xóa token
        return ResponseEntity.ok("Logged out");
    }
    
}
