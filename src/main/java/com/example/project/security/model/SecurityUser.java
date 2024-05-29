package com.example.project.security.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "security")
public class SecurityUser {
    @Id
    @SequenceGenerator(name = "securitySeqGen", sequenceName = "security_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "securitySeqGen")
    private Long id;

    @NotNull
    @Column(name = "user_id")
    private Long userId;

    //:TODO EMAIL
    @Size(min = 10, max = 40)
    @NotNull
    @Column(name = "login")
    private String login;

    @Size(min = 5, max = 40)
    @NotNull
    @Column(name = "password")
    private String password;
}
