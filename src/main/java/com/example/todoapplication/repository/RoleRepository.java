package com.example.todoapplication.repository;

import com.example.todoapplication.model.ERole;
import com.example.todoapplication.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);

    // Check if a role exists by name
    boolean existsByName(ERole name);

    // Custom query to find all admin roles
    @Query("SELECT r FROM Role r WHERE r.name = 'ROLE_ADMIN'")
    List<Role> findAllAdminRoles();
}