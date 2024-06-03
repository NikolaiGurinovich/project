package com.example.project.service;

import com.example.project.model.User;
import com.example.project.model.Workout;
import com.example.project.model.dto.AddWorkoutDto;
import com.example.project.repository.UserRepository;
import com.example.project.repository.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;

    @Autowired
    public WorkoutService(WorkoutRepository workoutRepository, UserRepository userRepository) {
        this.workoutRepository = workoutRepository;
        this.userRepository = userRepository;
    }

    public List<Workout> getAllWorkouts() {
        return workoutRepository.findAll();
    }

    public Optional<Workout> getWorkoutById(Long id) {
        return workoutRepository.findById(id);
    }

    public Boolean deleteWorkoutById(Long id) {
        Optional<Workout> workoutCheck = getWorkoutById(id);
        if (workoutCheck.isEmpty()){
            return false;
        }
        workoutRepository.deleteById(id);
        return workoutRepository.findById(id).isEmpty();
    }

    public Boolean createWorkout(Workout workout) {
        Workout newWorkout = new Workout();
        newWorkout.setWorkoutType(workout.getWorkoutType());
        newWorkout.setWorkoutDistance(workout.getWorkoutDistance());
        newWorkout.setWorkoutTime(workout.getWorkoutTime());
        if(userRepository.existsById(workout.getUserId())) {
            newWorkout.setUserId(workout.getUserId());
        } else {
            return false;
        }
        newWorkout.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        newWorkout.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        Workout createdWorkout = workoutRepository.save(newWorkout);
        return getWorkoutById(createdWorkout.getId()).isPresent();
    }

    public Boolean updateWorkout(Workout workout) {
        Optional<Workout> workoutFromDBOptional = workoutRepository.findById(workout.getId());
        if (workoutFromDBOptional.isPresent()){
            Workout workoutFromDB = workoutFromDBOptional.get();
            if (workoutFromDB.getWorkoutType() != null) {
                workoutFromDB.setWorkoutType(workout.getWorkoutType());
            } else return false;
            if (workoutFromDB.getWorkoutTime() != null){
                workoutFromDB.setWorkoutTime(workout.getWorkoutTime());
            } else return false;
            if (workoutFromDB.getWorkoutDistance() != null){
                workoutFromDB.setWorkoutDistance(workout.getWorkoutDistance());
            } else return false;
            if (userRepository.existsById(workout.getUserId())) {
                workoutFromDB.setUserId(workout.getUserId());
            } else return false;
            workoutFromDB.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
            Workout updatedWorkout = workoutRepository.saveAndFlush(workoutFromDB);
            return workoutFromDB.equals(updatedWorkout);
        }
        return false;
    }

    public Boolean addWorkoutFromUser(Long userId, AddWorkoutDto addWorkoutDto) {
        Optional<User> userFromDBOptional = userRepository.findById(userId);
        if (userFromDBOptional.isEmpty()){
            return false;
        }
        Workout newWorkout = new Workout();
        newWorkout.setWorkoutType(addWorkoutDto.getWorkoutType());
        newWorkout.setWorkoutDistance(addWorkoutDto.getWorkoutDistance());
        newWorkout.setWorkoutTime(addWorkoutDto.getWorkoutTime());
        newWorkout.setUserId(userId);
        newWorkout.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        newWorkout.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        Workout createdWorkout = workoutRepository.save(newWorkout);
        return getWorkoutById(createdWorkout.getId()).isPresent();
    }
}
