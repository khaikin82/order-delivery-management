package com.khaikin.delivery.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import com.khaikin.delivery.dto.user.UserDto;
import com.khaikin.delivery.dto.user.UserUpdateDto;
import com.khaikin.delivery.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable @Positive Long id,
                                              @RequestBody @Valid UserUpdateDto userUpdateDto) {
        return ResponseEntity.ok(userService.updateUser(id, userUpdateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable @Positive Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

}
