package com.example.todoapplication.repository;
import com.example.todoapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Find by username (used for login)
    Optional<User> findByUsername(String username);

    // Find by email
    Optional<User> findByEmail(String email);

    // Check if username exists (for registration validation)
    Boolean existsByUsername(String username);

    // Check if email exists (for registration validation)
    Boolean existsByEmail(String email);

    // Find all users with a specific role
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<User> findAllByRoleName(String roleName);

    // Corrected query using proper JPA relationship path
    @Query("SELECT DISTINCT u FROM User u JOIN u.tasks t WHERE t.dueDate <= :deadline")
    List<User> findUsersWithTasksDueSoon(@Param("deadline") LocalDateTime deadline);

}



