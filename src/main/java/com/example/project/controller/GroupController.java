package com.example.project.controller;

import com.example.project.model.Group;
import com.example.project.model.User;
import com.example.project.model.dto.CreateGroupByUserDto;
import com.example.project.service.GroupService;
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

    @Autowired
    public GroupController(GroupService groupService, UserService userService) {
        this.groupService = groupService;
        this.userService = userService;
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
        return new ResponseEntity<>(groupService.createGroup(group) ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @PutMapping
    public ResponseEntity<HttpStatus> updateGroup(@RequestBody Group group) {
        log.info("start method updateGroup in GroupController");
        return new ResponseEntity<>(groupService.updateGroup(group) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUserById(@PathVariable("id") Long id) {
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
}
