package com.example.todoapplication.service;
import com.example.todoapplication.dto.request.SignupRequest;
import com.example.todoapplication.dto.request.UpdateUserRequest;
import com.example.todoapplication.dto.response.UserResponse;
import com.example.todoapplication.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void registerUser(SignupRequest signUpRequest);
    User updateUser(Long userId, UpdateUserRequest updateRequest);
    void deleteUser(Long userId);
    List<UserResponse> findAll();
    // After — return Optional<User>
      //Optional<User> findById(Long id);
    UserResponse findById(Long userId);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
     User getCurrentAuthenticatedUser();
    User updateUserRole(Long userId, String newRole);

}
