package com.example.project.service;

import com.example.project.model.LinkUserGroup;
import com.example.project.repository.GroupRepository;
import com.example.project.repository.LUserGroupRepository;
import com.example.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LUserGroupService {

    private final LUserGroupRepository lUserGroupRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupService groupService;

    @Autowired
    public LUserGroupService(LUserGroupRepository lUserGroupRepository, UserRepository userRepository, GroupRepository groupRepository, GroupService groupService) {
        this.lUserGroupRepository = lUserGroupRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.groupService = groupService;
    }

    public List<LinkUserGroup> getAllLinks() {
        return lUserGroupRepository.findAll();
    }

    public Optional<LinkUserGroup> getLinkById(Long id) {
        return lUserGroupRepository.findById(id);
    }

    public Boolean deleteLinkById(Long id) {
        Optional<LinkUserGroup> linkUserGroup = lUserGroupRepository.findById(id);
        if (linkUserGroup.isEmpty()) {
            return false;
        }
        lUserGroupRepository.deleteById(id);
        return lUserGroupRepository.findById(id).isEmpty();
    }

    public Boolean createLink(LinkUserGroup linkUserGroup) {
        LinkUserGroup newLink = new LinkUserGroup();
        if (lUserGroupRepository.existsAllByUserIdAndGroupId(linkUserGroup.getUserId(), linkUserGroup.getGroupId())) {
            return false;
        }
        if (userRepository.existsById(linkUserGroup.getUserId())) {
            newLink.setGroupId(linkUserGroup.getGroupId());
        } else return false;
        if (groupRepository.existsById(linkUserGroup.getGroupId())) {
            newLink.setUserId(linkUserGroup.getUserId());
        } else return false;
        LinkUserGroup savedLink = lUserGroupRepository.save(newLink);
        return getLinkById(savedLink.getId()).isPresent();
    }

    public Boolean updateLink(LinkUserGroup linkUserGroup) {
        Optional<LinkUserGroup> linkFromDBOptional = lUserGroupRepository.findById(linkUserGroup.getId());
        if (linkFromDBOptional.isPresent()) {
            if (lUserGroupRepository.existsAllByUserIdAndGroupId(linkUserGroup.getUserId(), linkUserGroup.getGroupId())) {
                return false;
            }
            LinkUserGroup linkFromDB = linkFromDBOptional.get();
            if (userRepository.existsById(linkUserGroup.getUserId())) {
                linkFromDB.setGroupId(linkUserGroup.getGroupId());
            } else return false;
            if (groupRepository.existsById(linkUserGroup.getGroupId())) {
                linkFromDB.setUserId(linkUserGroup.getUserId());
            } else return false;
            LinkUserGroup updatedLink = lUserGroupRepository.saveAndFlush(linkFromDB);
            return linkFromDB.equals(updatedLink);
        }
        return false;
    }

    @Transactional
    public Boolean joinGroup(Long groupID, Long userID) {
        if (lUserGroupRepository.existsAllByUserIdAndGroupId(userID, groupID)) {
            return false;
        }
        if (groupRepository.findById(groupID).get().getGroupAdminID().equals(userID)) {
            return false;
        }
        LinkUserGroup newLink = new LinkUserGroup();
        if(userID != null) {
            newLink.setUserId(userID);
        }
        if(groupID != null) {
            newLink.setGroupId(groupID);
        }
        groupService.getGroupById(groupID).get().setNumberOfMembers(groupService.getGroupById(groupID).
                    get().getNumberOfMembers() + 1);
        LinkUserGroup savedLink = lUserGroupRepository.saveAndFlush(newLink);
        return getLinkById(savedLink.getId()).isPresent();
    }

    @Transactional
    public Boolean leaveGroup(Long groupID, Long userID) {
        if (lUserGroupRepository.existsAllByUserIdAndGroupId(userID, groupID)) {
            groupService.getGroupById(groupID).get().setNumberOfMembers(groupService.getGroupById(groupID).
                    get().getNumberOfMembers() - 1);
           return deleteLinkById(lUserGroupRepository.findAllByUserIdAndGroupId(userID, groupID).get().getId());
        }
        return false;
    }

    @Transactional
    public Boolean deleteUserFromMyGroup(Long userID, Long groupID, Long groupAdminId) {
        if (checkTable(userID, groupID)) {
            if(groupAdminId.equals(groupService.getGroupById(groupID).get().getGroupAdminID())){
                groupService.getGroupById(groupID).get().setNumberOfMembers(groupService.getGroupById(groupID).
                        get().getNumberOfMembers() - 1);
                return deleteLinkById(lUserGroupRepository.findAllByUserIdAndGroupId(userID, groupID).get().getId());
            }
            return false;
        }
        return false;
    }

    @Transactional
    public Boolean deleteUserFromGroup(Long userID, Long groupID) {
        if (checkTable(userID, groupID)) {
                groupService.getGroupById(groupID).get().setNumberOfMembers(groupService.getGroupById(groupID).
                        get().getNumberOfMembers() - 1);
                return deleteLinkById(lUserGroupRepository.findAllByUserIdAndGroupId(userID, groupID).get().getId());
        }
        return false;
    }

    private Boolean checkTable(Long userID, Long groupID) {
        if (lUserGroupRepository.existsAllByUserIdAndGroupId(userID, groupID)) {
            return true;
        }
        return false;
    }
}
