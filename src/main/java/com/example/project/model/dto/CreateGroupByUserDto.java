package com.example.project.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateGroupByUserDto {
    @NotNull
    private String groupName;

}
