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
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Group>> getAllGroups() {
        return new ResponseEntity<>(groupService.getAllGroups(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GROUP_ADMIN')")
    public ResponseEntity<Group> getGroupById(@PathVariable("id") Long id) {
        log.info("start method getGroupById in GroupController");
        Optional<Group> group = groupService.getGroupById(id);
        if (group.isPresent()) {
            return new ResponseEntity<>(group.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteGroupById(@PathVariable("id") Long id) {
        log.info("start method deleteGroupById in GroupController");
        return new ResponseEntity<>(groupService.deleteGroupById(id) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GROUP_ADMIN')")
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

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GROUP_ADMIN')")
    public ResponseEntity<HttpStatus> updateMyGroupName(@PathVariable("id") Long id,
                                                        @RequestBody @Valid CreateGroupByUserDto createGroupByUserDto,
                                                        BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            log.error(bindingResult.getFieldError().getDefaultMessage());
        }
        if (checkBody(principal, id)) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(groupService.updateMyGroupName(id, createGroupByUserDto, principal)
                ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @PostMapping("/join/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GROUP_ADMIN')")
    public ResponseEntity<HttpStatus> joinGroup(@PathVariable("id") Long id, Principal principal) {
        log.info("start method joinGroup in GroupController");
        if (checkBody(principal, id)) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(lUserGroupService.joinGroup(id,
                userService.getInfoAboutCurrentUser(principal.getName()).get().getId())
                ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @DeleteMapping("/leave/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GROUP_ADMIN')")
    public ResponseEntity<HttpStatus> leaveGroup(@PathVariable("id") Long id, Principal principal) {
        log.info("start method leaveGroup in GroupController");
        if (checkBody(principal, id)) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(lUserGroupService.leaveGroup(id,
                userService.getInfoAboutCurrentUser(principal.getName()).get().getId())
                ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @DeleteMapping("/user/{userId}/{groupId}")
    @PreAuthorize("hasRole('GROUP_ADMIN')")
    public ResponseEntity<HttpStatus> deleteUserFromMyGroup(@PathVariable("userId") Long userId,
                                                            @PathVariable("groupId") Long groupId,
                                                            Principal principal) {
        log.info("start method deleteUserFromMyGroup in GroupController");
        if (checkBody(principal, groupId)) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if(userService.getUserById(userId).isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lUserGroupService.deleteUserFromMyGroup(userId, groupId,
                userService.getInfoAboutCurrentUser(principal.getName()).get().getId())
                ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('GROUP_ADMIN')")
    public ResponseEntity<HttpStatus> deleteGroupByGroupAdmin(@PathVariable("id") Long id, Principal principal) {
        log.info("start method deleteGroup in GroupController");
        if (checkBody(principal, id)) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(groupService.deleteGroupByGroupAdmin(id,
                userService.getInfoAboutCurrentUser(principal.getName()).get().getId())
                ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @DeleteMapping("/user/{userId}/group/{groupId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity <HttpStatus> deleteUserFromGroup(@PathVariable("userId") Long userId,
                                                           @PathVariable("groupId") Long groupId
            , Principal principal) {
        log.info("start method deleteUserFromGroup in GroupController");
        Optional<User> admin = userService.getInfoAboutCurrentUser(principal.getName());
        if (admin.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<User> user = userService.getUserById(userId);
        if (user.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<Group> group = groupService.getGroupById(groupId);
        if (group.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lUserGroupService.deleteUserFromGroup(userId, groupId)
                ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    private boolean checkBody(@RequestBody Principal principal, Long id) {
        Optional<User> user = userService.getInfoAboutCurrentUser(principal.getName());
        if (user.isEmpty()){
            return true;
        }
        Optional<Group> targetGroup = groupService.getGroupById(id);
        if (targetGroup.isEmpty()) {
            return true;
        }
        return false;
    }
}
