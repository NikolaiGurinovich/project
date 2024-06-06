package com.example.project.repository;

import com.example.project.model.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {
    Boolean existsAllByUserIdAndWorkoutId(Long userId, Long workoutId);
}
