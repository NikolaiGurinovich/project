package com.example.project.model;

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
@Entity(name = "likes")
public class Likes {
    @Id
    @SequenceGenerator(name = "likesSeqGen", sequenceName = "likes_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "likesSeqGen")
    private Long id;

    @NotNull
    @Column(name = "workout_id")
    private Long workoutId;

    @NotNull
    @Column(name = "user_id")
    private Long userId;
}
