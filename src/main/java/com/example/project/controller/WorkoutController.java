package com.example.project.controller;

import com.example.project.model.User;
import com.example.project.model.Workout;
import com.example.project.service.WorkoutService;
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
@RequestMapping("/workout")
public class WorkoutController {
    private final WorkoutService workoutService;

    @Autowired
    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
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
}
