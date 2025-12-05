package com.jobportal.auth_service.repository;

import com.jobportal.auth_service.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(@Email @Max(30) String email);

    boolean existsByEmail(@Email @Max(30) String email);
}
