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
@Entity(name = "followers")
public class Followers {
    @Id
    @SequenceGenerator(name = "followersSeqGen", sequenceName = "followers_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "followersSeqGen")
    private Long id;

    @NotNull
    @Column(name = "sub_user_id")
    private Long subUserId;

    @NotNull
    @Column(name = "user_id")
    private Long userId;
}
