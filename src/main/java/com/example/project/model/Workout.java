package com.example.project.model;

import com.example.project.enums.Gender;
import com.example.project.enums.WorkoutType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "workout")
public class Workout {
    @Id
    @SequenceGenerator(name = "workoutSeqGen", sequenceName = "workout_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "workoutSeqGen")
    private Long id;

    @NotNull
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "workout_distance")
    private Double workoutDistance;

    //:TODO format for time?
    @NotNull
    @Column(name = "workout_time")
    private Double workoutTime;

    @Enumerated
    @NotNull
    @Column(name = "workout_type")
    private WorkoutType workoutType;
}
