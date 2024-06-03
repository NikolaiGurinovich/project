package com.example.project.controller;

import com.example.project.model.Followers;
import com.example.project.service.FollowersService;
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
@RequestMapping("/followers")
public class FollowersController {
    private final FollowersService followersService;

    @Autowired
    public FollowersController(FollowersService followersService) {
        this.followersService = followersService;
    }

    @GetMapping
    public ResponseEntity<List<Followers>> getAllFollowers() {
        log.info("start getAllFollowers from FollowersController");
        return new ResponseEntity<>(followersService.getAllFollowers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Followers> getFollowById(@PathVariable("id") Long id) {
        log.info("start getFollowersById from FollowersController");
        Optional<Followers> followers = followersService.getFollowById(id);
        if (followers.isPresent()) {
            return new ResponseEntity<>(followers.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createFollow(@RequestBody @Valid Followers followers, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error(bindingResult.getFieldError().getDefaultMessage());
        }
        return new ResponseEntity<>(followersService.createFollow(followers) ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @PutMapping
    public ResponseEntity<HttpStatus> updateFollow(@RequestBody Followers followers) {
        log.info("start updateFollow from FollowersController");
        return new ResponseEntity<>(followersService.updateFollow(followers) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteFollowById(@PathVariable("id") Long id) {
        log.info("start deleteFollowById from FollowersController");
        return new ResponseEntity<>(followersService.deleteFollowById(id) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }
}

