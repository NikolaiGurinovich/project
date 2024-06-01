package com.example.project.security.model.dto;

import com.example.project.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class RegistrationDto {

    @Email
    @NotNull
    private String login;

    @Size(min = 3)
    @NotNull
    private String password;

    @Size(min = 1, max = 30)
    @NotNull
    private String userName;

    @Min(1)
    @Max(120)
    @NotNull
    private Integer userAge;

    @Positive
    @NotNull
    private Double userWeight;

    @NotNull
    private Gender gender;
}
