package com.example.project.repository;

import com.example.project.model.LinkUserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LUserGroupRepository extends JpaRepository<LinkUserGroup, Long> {
}
