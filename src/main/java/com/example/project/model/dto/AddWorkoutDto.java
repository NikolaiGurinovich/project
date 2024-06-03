package com.example.project.model.dto;

import com.example.project.enums.WorkoutType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Data
@Component
public class AddWorkoutDto {

    @Column(name = "workout_distance")
    private Double workoutDistance;

    @NotNull
    @Column(name = "workout_time")
    private Long workoutTime;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "workout_type")
    private WorkoutType workoutType;

    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp created;

    @Column(name = "updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp updated;
}
