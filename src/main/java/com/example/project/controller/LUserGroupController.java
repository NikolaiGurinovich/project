package com.example.project.controller;

import com.example.project.model.LinkUserGroup;
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

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/link")
public class LUserGroupController {

    private final LUserGroupService lUserGroupService;
    private final UserService userService;
    private final GroupService groupService;

    @Autowired
    public LUserGroupController(LUserGroupService lUserGroupService, UserService userService, GroupService groupService) {
        this.lUserGroupService = lUserGroupService;
        this.userService = userService;
        this.groupService = groupService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LinkUserGroup>> getAllLinks() {
        log.info("start method getAllLinks from LUserGroupController");
        return new ResponseEntity<>(lUserGroupService.getAllLinks(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LinkUserGroup> getLinkById(@PathVariable("id") Long id) {
        log.info("start method getLinkById from LUserGroupController");
        Optional<LinkUserGroup> linkUserGroup = lUserGroupService.getLinkById(id);
        if (linkUserGroup.isPresent()) {
            return new ResponseEntity<>(linkUserGroup.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> createLink(@RequestBody @Valid LinkUserGroup linkUserGroup,
                                                 BindingResult bindingResult) {
        log.info("start method createLink from LUserGroupController");
        if (bindingResult.hasErrors()) {
            log.error(bindingResult.getFieldError().getDefaultMessage());
        }
        if (checkBody(linkUserGroup)) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(lUserGroupService.
                createLink(linkUserGroup) ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> updateLink(@RequestBody @Valid LinkUserGroup linkUserGroup) {
        log.info("start method updateLink from LUserGroupController");
        if (checkBody(linkUserGroup)) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (lUserGroupService.getLinkById(linkUserGroup.getId()).isEmpty()) {
            log.error("link id not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lUserGroupService.
                updateLink(linkUserGroup) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    private boolean checkBody(@RequestBody LinkUserGroup linkUserGroup) {
        if (userService.getUserById(linkUserGroup.getUserId()).isEmpty()) {
            log.error("user not found");
            return true;
        }
        if (groupService.getGroupById(linkUserGroup.getGroupId()).isEmpty()) {
            log.error("group not found");
            return true;
        }
        return false;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteLinkById(@PathVariable("id") Long id) {
        log.info("start method deleteLinkById from LUserGroupController");
        return new ResponseEntity<>(lUserGroupService.deleteLinkById(id) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }
}
