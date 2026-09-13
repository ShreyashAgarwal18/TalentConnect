package com.Project.TalentConnect.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GigDescriptionRequestDto {

    @NotBlank(message = "Title cannot be empty")
    @Size(max = 150, message = "Title cannot exceed 150 characters")
    private String title;

    @NotBlank(message = "Category cannot be empty")
    @Size(max = 100, message = "Category cannot exceed 100 characters")
    private String category;

    @NotBlank(message = "Skills cannot be empty")
    @Size(max = 300, message = "Skills cannot exceed 300 characters")
    private String skills;
}