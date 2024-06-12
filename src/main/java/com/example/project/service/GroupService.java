package com.example.project.service;

import com.example.project.model.Group;
import com.example.project.model.dto.CreateGroupByUserDto;
import com.example.project.repository.GroupRepository;
import com.example.project.repository.UserRepository;
import com.example.project.security.enums.Roles;
import com.example.project.security.repository.UserSecurityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    private static final Logger log = LoggerFactory.getLogger(GroupService.class);
    private final GroupRepository groupRepository;
    private final UserSecurityRepository userSecurityRepository;
    private final UserRepository userRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository, UserSecurityRepository userSecurityRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userSecurityRepository = userSecurityRepository;
        this.userRepository = userRepository;
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
        if(groupRepository.existsByGroupName(group.getGroupName())){
            return false;
        }
        newGroup.setGroupName(group.getGroupName());
        if(!userRepository.existsById(group.getGroupAdminID())){
            return false;
        }
        newGroup.setGroupAdminID(group.getGroupAdminID());
        newGroup.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        newGroup.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        Group savedGroup = groupRepository.save(newGroup);
        return getGroupById(savedGroup.getId()).isPresent();
    }

    public Boolean updateGroup(Group group) {
        Optional<Group> groupFromDBOptional = groupRepository.findById(group.getId());
        if (groupFromDBOptional.isPresent()) {
            Group groupFromDB = groupFromDBOptional.get();
            if (group.getGroupName() != null && !groupRepository.existsByGroupName(group.getGroupName())){
                groupFromDB.setGroupName(group.getGroupName());
            } else return false;
            if(!userRepository.existsById(group.getGroupAdminID())){
                return false;
            }
            groupFromDB.setGroupAdminID(group.getGroupAdminID());
            groupFromDB.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
            Group updatedGroup = groupRepository.saveAndFlush(groupFromDB);
            return groupFromDB.equals(updatedGroup);
        }
        return false;
    }

    @Transactional
    public Boolean createGroupByUser (CreateGroupByUserDto createGroupByUserDto, String userLogin) {
        Group group = new Group();
        if (createGroupByUserDto.getGroupName() != null
                && !groupRepository.existsByGroupName(createGroupByUserDto.getGroupName())) {
            group.setGroupName(createGroupByUserDto.getGroupName());
        }
        group.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        group.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        group.setGroupAdminID(userSecurityRepository.findByUserLogin(userLogin).get().getUserId());
        group.setNumberOfMembers(1L);
        userSecurityRepository.findByUserLogin(userLogin).get().setRole(Roles.GROUP_ADMIN);
        Group savedGroup = groupRepository.save(group);
        return getGroupById(savedGroup.getId()).isPresent();
    }

    public Boolean deleteGroupByGroupAdmin (Long groupID, Long groupAdminID) {
        Optional<Group> groupFromDBOptional = groupRepository.findById(groupID);
        if (groupFromDBOptional.isEmpty()) {
            return false;
        }
        Group groupFromDB = groupFromDBOptional.get();
        if (groupFromDB.getGroupAdminID().equals(groupAdminID)) {
            deleteGroupById(groupID);
        }
        return true;
    }
}
