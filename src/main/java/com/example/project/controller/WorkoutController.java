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
    public ResponseEntity<List<Workout>> getAllWorkouts() {
        log.info("start getAllWorkouts in WorkoutController");
        return new ResponseEntity<>(workoutService.getAllWorkouts(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Workout> getWorkoutById(@PathVariable("id") Long id) {
        log.info("start getWorkoutById in WorkoutController");
        Optional<Workout> workout = workoutService.getWorkoutById(id);
        if (workout.isPresent()) {
            return new ResponseEntity<>(workout.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createWorkout(@RequestBody @Valid Workout workout, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error(bindingResult.getFieldError().getDefaultMessage());
        }
        return new ResponseEntity<>(workoutService.createWorkout(workout) ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @PutMapping
    public ResponseEntity<HttpStatus> updateWorkout(@RequestBody Workout workout) {
        log.info("start updateWorkout in WorkoutController");
        return new ResponseEntity<>(workoutService.updateWorkout(workout) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteWorkoutById(@PathVariable("id") Long id) {
        log.info("start deleteWorkoutById in WorkoutController");
        return new ResponseEntity<>(workoutService.deleteWorkoutById(id) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @PostMapping("/add")
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
