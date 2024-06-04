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
        LinkUserGroup linkUserGroup = new LinkUserGroup();
        linkUserGroup.setUserId(userID);
        linkUserGroup.setGroupId(groupID);
        groupService.getGroupById(groupID).get().setNumberOfMembers(groupService.getGroupById(groupID).
                get().getNumberOfMembers() + 1);
        LinkUserGroup savedLink = lUserGroupRepository.saveAndFlush(linkUserGroup);
        return getLinkById(savedLink.getId()).isPresent();
    }
}
