package com.example.project.model.dto;

import com.example.project.enums.WorkoutType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddWorkoutDto {

    private Double workoutDistance;

    @NotNull
    private Long workoutTime;

    @Enumerated(EnumType.STRING)
    @NotNull
    private WorkoutType workoutType;
}
