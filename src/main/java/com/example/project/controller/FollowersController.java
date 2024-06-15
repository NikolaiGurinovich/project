package com.example.project.controller;

import com.example.project.model.Followers;
import com.example.project.service.FollowersService;
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
@RequestMapping("/followers")
public class FollowersController {

    private final FollowersService followersService;
    private final UserService userService;

    @Autowired
    public FollowersController(FollowersService followersService, UserService userService) {
        this.followersService = followersService;
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Followers>> getAllFollowers() {
        log.info("start getAllFollowers from FollowersController");
        return new ResponseEntity<>(followersService.getAllFollowers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Followers> getFollowById(@PathVariable("id") Long id) {
        log.info("start getFollowersById from FollowersController");
        Optional<Followers> followers = followersService.getFollowById(id);
        if (followers.isPresent()) {
            return new ResponseEntity<>(followers.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> createFollow(@RequestBody @Valid Followers followers, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error(bindingResult.getFieldError().getDefaultMessage());
        }
        if (checkBody(followers)) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(followersService.createFollow(followers) ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> updateFollow(@RequestBody @Valid Followers followers) {
        log.info("start updateFollow from FollowersController");
        if (checkBody(followers)) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(followersService.updateFollow(followers) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    private boolean checkBody(@RequestBody Followers followers) {
        if(userService.getUserById(followers.getUserId()).isEmpty()){
            log.error("user not found");
            return true;
        }
        if(userService.getUserById(followers.getSubUserId()).isEmpty()){
            log.error("sub user not found");
            return true;
        }
        return false;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteFollowById(@PathVariable("id") Long id) {
        log.info("start deleteFollowById from FollowersController");
        return new ResponseEntity<>(followersService.deleteFollowById(id) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }
}

