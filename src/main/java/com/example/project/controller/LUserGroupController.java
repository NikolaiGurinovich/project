package com.example.project.controller;

import com.example.project.model.LinkUserGroup;
import com.example.project.service.LUserGroupService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/link")
public class LUserGroupController {
    private final LUserGroupService lUserGroupService;

    @Autowired
    public LUserGroupController(LUserGroupService lUserGroupService) {
        this.lUserGroupService = lUserGroupService;
    }

    @GetMapping
    public ResponseEntity<List<LinkUserGroup>> getAllLinks() {
        log.info("start method getAllLinks from LUserGroupController");
        return new ResponseEntity<>(lUserGroupService.getAllLinks(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LinkUserGroup> getLinkById(@PathVariable("id") Long id) {
        log.info("start method getLinkById from LUserGroupController");
        Optional<LinkUserGroup> linkUserGroup = lUserGroupService.getLinkById(id);
        if (linkUserGroup.isPresent()) {
            return new ResponseEntity<>(linkUserGroup.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createLink(@RequestBody @Valid LinkUserGroup linkUserGroup,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error(bindingResult.getFieldError().getDefaultMessage());
        }
        return new ResponseEntity<>(lUserGroupService.
                createLink(linkUserGroup) ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @PutMapping
    public ResponseEntity<HttpStatus> updateLink(@RequestBody LinkUserGroup linkUserGroup) {
        log.info("start method updateLink from LUserGroupController");
        return new ResponseEntity<>(lUserGroupService.
                updateLink(linkUserGroup) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteLinkById(@PathVariable("id") Long id) {
        log.info("start method deleteLinkById from LUserGroupController");
        return new ResponseEntity<>(lUserGroupService.deleteLinkById(id) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }
}
