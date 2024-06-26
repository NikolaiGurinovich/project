package com.example.project.repository;

import com.example.project.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Boolean existsByGroupName(String name);
}
