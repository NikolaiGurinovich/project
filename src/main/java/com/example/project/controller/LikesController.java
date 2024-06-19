package com.example.project.controller;

import com.example.project.model.Likes;
import com.example.project.service.LikesService;
import com.example.project.service.UserService;
import com.example.project.service.WorkoutService;
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
@RequestMapping("/likes")
public class LikesController {

    private final LikesService likesService;
    private final UserService userService;
    private final WorkoutService workoutService;

    @Autowired
    public LikesController(LikesService likesService, UserService userService, WorkoutService workoutService) {
        this.likesService = likesService;
        this.userService = userService;
        this.workoutService = workoutService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Likes>> getAllLikes() {
        log.info("start getAllLikes from LikesController");
        return new ResponseEntity<>(likesService.getAllLikes(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Likes> getLikeById(@PathVariable("id") Long id) {
        log.info("start getLikeById from LikesController");
        Optional<Likes> like = likesService.getLikeById(id);
        if (like.isPresent()) {
            return new ResponseEntity<>(like.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> createLike(@RequestBody @Valid Likes like, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error(bindingResult.getFieldError().getDefaultMessage());
        }
        if (checkBody(like)) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(likesService.createLike(like) ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> updateLike(@RequestBody @Valid Likes like) {
        log.info("start updateLike from LikesController");
        if (checkBody(like)) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (likesService.getLikeById(like.getId()).isEmpty()) {
            log.error("like id not found ");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(likesService.updateLike(like) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    private boolean checkBody(@RequestBody Likes like) {
        if (workoutService.getWorkoutById(like.getWorkoutId()).isEmpty()) {
            log.error("workout id not found");
            return true;
        }
        if (userService.getUserById(like.getUserId()).isEmpty()) {
            log.error("user not found");
            return true;
        }
        return false;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteLikeById(@PathVariable("id") Long id) {
        log.info("start deleteLikeById from LikesController");
        return new ResponseEntity<>(likesService.deleteLikeById(id) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }
}
