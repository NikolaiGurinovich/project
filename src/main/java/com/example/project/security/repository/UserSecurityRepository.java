package com.example.project.security.repository;

import com.example.project.security.model.SecurityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSecurityRepository extends JpaRepository<SecurityUser, Long> {
    Optional<SecurityUser> findByUserLogin(String userLogin);

    Optional<SecurityUser> findByUserId(Long userId);
}
