package com.example.project.controller;

import com.example.project.model.Group;
import com.example.project.model.User;
import com.example.project.model.dto.CreateGroupByUserDto;
import com.example.project.service.GroupService;
import com.example.project.service.LUserGroupService;
import com.example.project.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/group")
public class GroupController {

    private final GroupService groupService;
    private final UserService userService;
    private final LUserGroupService lUserGroupService;

    @Autowired
    public GroupController(GroupService groupService, UserService userService, LUserGroupService lUserGroupService) {
        this.groupService = groupService;
        this.userService = userService;
        this.lUserGroupService = lUserGroupService;
    }

    @GetMapping
    public ResponseEntity<List<Group>> getAllGroups() {
        return new ResponseEntity<>(groupService.getAllGroups(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroupById(@PathVariable("id") Long id) {
        log.info("start method getGroupById in GroupController");
        Optional<Group> group = groupService.getGroupById(id);
        if (group.isPresent()) {
            return new ResponseEntity<>(group.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createGroup(@RequestBody @Valid Group group, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error(bindingResult.getFieldError().getDefaultMessage());
        }
        if(userService.getUserById(group.getGroupAdminID()).isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(groupService.createGroup(group) ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @PutMapping
    public ResponseEntity<HttpStatus> updateGroup(@RequestBody @Valid Group group) {
        log.info("start method updateGroup in GroupController");
        if(userService.getUserById(group.getGroupAdminID()).isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (groupService.getGroupById(group.getId()).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(groupService.updateGroup(group) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteGroupById(@PathVariable("id") Long id) {
        log.info("start method deleteGroupById in GroupController");
        return new ResponseEntity<>(groupService.deleteGroupById(id) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createGroupByUser(@RequestBody @Valid CreateGroupByUserDto createGroupByUserDto
            , BindingResult bindingResult, Principal principal) {
        log.info("start method createGroupByUser in GroupController");
        if (bindingResult.hasErrors()) {
            log.error(bindingResult.getFieldError().getDefaultMessage());
        }
        Optional<User> creator = userService.getInfoAboutCurrentUser(principal.getName());
        if (creator.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new  ResponseEntity<>(groupService.createGroupByUser(createGroupByUserDto, principal.getName())
                ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @PostMapping("/join/{id}")
    public ResponseEntity<HttpStatus> joinGroup(@PathVariable("id") Long id, Principal principal) {
        log.info("start method joinGroup in GroupController");
        Optional<User> newMember = userService.getInfoAboutCurrentUser(principal.getName());
        if (newMember.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<Group> targetGroup = groupService.getGroupById(id);
        if (targetGroup.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lUserGroupService.joinGroup(id, newMember.get().getId())
                ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @DeleteMapping("/leave/{id}")
    public ResponseEntity<HttpStatus> leaveGroup(@PathVariable("id") Long id, Principal principal) {
        log.info("start method leaveGroup in GroupController");
        Optional<User> member = userService.getInfoAboutCurrentUser(principal.getName());
        if (member.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<Group> targetGroup = groupService.getGroupById(id);
        if (targetGroup.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lUserGroupService.leaveGroup(id, member.get().getId())
                ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @DeleteMapping("/user/{userId}/{groupId}")
    public ResponseEntity<HttpStatus> deleteUserFromMyGroup(@PathVariable("userId") Long userId,
                                                            @PathVariable("groupId") Long groupId,
                                                            Principal principal) {
        log.info("start method deleteUserFromMyGroup in GroupController");
        Optional<User> groupAdmin = userService.getInfoAboutCurrentUser(principal.getName());
        if (groupAdmin.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<Group> targetGroup = groupService.getGroupById(groupId);
        if (targetGroup.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lUserGroupService.deleteUserFromMyGroup(userId, groupId, groupAdmin.get().getId())
                ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }
}
