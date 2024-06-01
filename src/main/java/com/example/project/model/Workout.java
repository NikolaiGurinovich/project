package com.example.project.model;

import com.example.project.enums.WorkoutType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;

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

    //TODO: Change data type
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
