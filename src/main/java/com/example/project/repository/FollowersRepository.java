package com.example.project.repository;

import com.example.project.model.Followers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowersRepository extends JpaRepository<Followers, Long> {
    Boolean existsAllByUserIdAndSubUserId(Long userId, Long subUserId);

    Optional<Followers> findAllByUserIdAndSubUserId(Long userId, Long subUserId);
}
