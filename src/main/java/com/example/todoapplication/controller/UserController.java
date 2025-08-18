package com.example.todoapplication.controller;

import com.example.todoapplication.dto.request.UpdateUserRequest;
import com.example.todoapplication.dto.response.UserResponse;
import com.example.todoapplication.exception.CustomException;
import com.example.todoapplication.model.User;
import com.example.todoapplication.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public UserResponse updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest updateRequest) {
        User user = userService.updateUser(id, updateRequest);
        return convertToResponse(user);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
    // to update the user role only admin access this endpoint

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse updateUserRole(
            @PathVariable Long id,
            @RequestParam String newRole) {

        User updatedUser = userService.updateUserRole(id, newRole);
        return convertToResponse(updatedUser);
    }

    private UserResponse convertToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toSet()));
    }
}