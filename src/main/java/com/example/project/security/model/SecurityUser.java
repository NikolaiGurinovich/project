package com.example.project.security.model;

import com.example.project.security.enums.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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

    @Email
    @NotNull
    @Column(name = "login")
    private String userLogin;

    @Size(min = 3)
    @NotNull
    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Roles role;
}
