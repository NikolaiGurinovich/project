package com.example.project.service;

import com.example.project.model.Group;
import com.example.project.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    private final GroupRepository groupRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Optional<Group> getGroupById(Long id) {
        return groupRepository.findById(id);
    }

    public Boolean deleteGroupById(Long id) {
        Optional<Group> group = groupRepository.findById(id);
        if (group.isEmpty()) {
           return false;
        }
        groupRepository.deleteById(id);
        return groupRepository.findById(id).isEmpty();
    }

    public Boolean createGroup(Group group) {
        Group newGroup = new Group();
        newGroup.setGroupName(group.getGroupName());
        newGroup.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        newGroup.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        Group savedGroup = groupRepository.save(newGroup);
        return getGroupById(savedGroup.getId()).isPresent();
    }

    public Boolean updateGroup(Group group) {
        Optional<Group> groupFromDBOptional = groupRepository.findById(group.getId());
        if (groupFromDBOptional.isPresent()) {
            Group groupFromDB = groupFromDBOptional.get();
            if (group.getGroupName() != null){
                groupFromDB.setGroupName(group.getGroupName());
            }
            groupFromDB.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
            Group updatedGroup = groupRepository.saveAndFlush(groupFromDB);
            return groupFromDB.equals(updatedGroup);
        }
        return false;
    }
}
