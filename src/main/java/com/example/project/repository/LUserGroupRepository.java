package com.example.project.repository;

import com.example.project.model.LinkUserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LUserGroupRepository extends JpaRepository<LinkUserGroup, Long> {
    Boolean existsAllByUserIdAndGroupId(Long userId, Long groupId);

    Optional<LinkUserGroup> findAllByUserIdAndGroupId(Long userId, Long groupId);
}
