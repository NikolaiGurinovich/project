package com.example.project.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class CreateGroupByUserDto {
    @NotNull
    @Size(min = 1, max = 300)
    private String groupName;

}
