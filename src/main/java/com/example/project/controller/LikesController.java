package com.example.project.controller;

import com.example.project.model.Likes;
import com.example.project.service.LikesService;
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
@RequestMapping("/likes")
public class LikesController {
    private final LikesService likesService;

    @Autowired
    public LikesController(LikesService likesService) {
        this.likesService = likesService;
    }

    @GetMapping
    public ResponseEntity<List<Likes>> getAllLikes() {
        log.info("start getAllLikes from LikesController");
        return new ResponseEntity<>(likesService.getAllLikes(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Likes> getLikeById(@PathVariable("id") Long id) {
        log.info("start getLikeById from LikesController");
        Optional<Likes> like = likesService.getLikeById(id);
        if (like.isPresent()) {
            return new ResponseEntity<>(like.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createLike(@RequestBody @Valid Likes like, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error(bindingResult.getFieldError().getDefaultMessage());
        }
        return new ResponseEntity<>(likesService.createLike(like) ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @PutMapping
    public ResponseEntity<HttpStatus> updateLike(@RequestBody Likes like) {
        log.info("start updateLike from LikesController");
        return new ResponseEntity<>(likesService.updateLike(like) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteLikeById(@PathVariable("id") Long id) {
        log.info("start deleteLikeById from LikesController");
        return new ResponseEntity<>(likesService.deleteLikeById(id) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }
}
