package com.example.project.controller;

import com.example.project.model.User;
import com.example.project.model.Workout;
import com.example.project.model.dto.AddWorkoutDto;
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

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/workout")
public class WorkoutController {
    private final WorkoutService workoutService;
    private final UserService userService;
    private final LikesService likesService;

    @Autowired
    public WorkoutController(WorkoutService workoutService, UserService userService, LikesService likesService) {
        this.workoutService = workoutService;
        this.userService = userService;
        this.likesService = likesService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Workout>> getAllWorkouts() {
        log.info("start getAllWorkouts in WorkoutController");
        return new ResponseEntity<>(workoutService.getAllWorkouts(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GROUP_ADMIN')")
    public ResponseEntity<Workout> getWorkoutById(@PathVariable("id") Long id) {
        log.info("start getWorkoutById in WorkoutController");
        Optional<Workout> workout = workoutService.getWorkoutById(id);
        if (workout.isPresent()) {
            return new ResponseEntity<>(workout.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> createWorkout(@RequestBody @Valid Workout workout, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error(bindingResult.getFieldError().getDefaultMessage());
        }
        return new ResponseEntity<>(workoutService.createWorkout(workout) ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> updateWorkout(@RequestBody @Valid Workout workout) {
        log.info("start updateWorkout in WorkoutController");
        return new ResponseEntity<>(workoutService.updateWorkout(workout) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteWorkoutById(@PathVariable("id") Long id) {
        log.info("start deleteWorkoutById in WorkoutController");
        return new ResponseEntity<>(workoutService.deleteWorkoutById(id) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GROUP_ADMIN')")
    public ResponseEntity<HttpStatus> addWorkout(@RequestBody @Valid AddWorkoutDto addWorkoutDto, Principal principal) {
        log.info("start addWorkout in WorkoutController");
        Optional<User> user = userService.getInfoAboutCurrentUser(principal.getName());
        if (user.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(workoutService.addWorkoutFromUser(user.get().getId(), addWorkoutDto)
                ? HttpStatus.CREATED : HttpStatus.CONFLICT );
    }

    @PostMapping("/like/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GROUP_ADMIN')")
    public ResponseEntity<HttpStatus> likeWorkout(@PathVariable("id") Long id, Principal principal) {
        log.info("start likeWorkout in WorkoutController");
        Optional<User> user = userService.getInfoAboutCurrentUser(principal.getName());
        if (user.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (workoutService.getWorkoutById(id).isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(likesService.likeWorkout(id,user.get().getId()) ? HttpStatus.CREATED : HttpStatus.CONFLICT );
    }
}
