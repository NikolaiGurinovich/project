package com.example.project.model.dto;

import com.example.project.enums.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserDto {
    @Size(min = 1, max = 30)
    private String userName;

    @Min(1)
    @Max(120)
    private Integer userAge;

    @Positive
    private Double userWeight;

    @Enumerated(EnumType.STRING)
    private Gender gender;
}
