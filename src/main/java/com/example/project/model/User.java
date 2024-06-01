package com.example.project.model;

import com.example.project.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

    @Component
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity(name = "users")
    public class User {
        @Id
        @SequenceGenerator(name = "userSeqGen", sequenceName = "users_id_seq", allocationSize = 1)
        @GeneratedValue(generator = "userSeqGen")
        private Long id;

        @NotNull
        @Size(min = 1, max = 30)
        @Column(name = "user_name", unique = true)
        private String userName;

        @Min(1)
        @Max(120)
        @NotNull
        @Column(name = "user_age")
        private Integer userAge;

        @Positive
        @NotNull
        @Column(name = "user_weight")
        private Double userWeight;

        @Column(name = "created")
        @Temporal(TemporalType.TIMESTAMP)
        private Timestamp created;

        @Enumerated(EnumType.STRING)
        @Column(name = "gender")
        private Gender gender;

        @Column(name = "updated")
        @Temporal(TemporalType.TIMESTAMP)
        private Timestamp updated;


    }

