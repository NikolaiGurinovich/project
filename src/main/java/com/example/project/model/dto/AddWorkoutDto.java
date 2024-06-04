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

    private Double workoutDistance;

    @NotNull
    private Long workoutTime;

    @Enumerated(EnumType.STRING)
    @NotNull
    private WorkoutType workoutType;

    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp created;

    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp updated;
}
