package com.Project.TalentConnect.DTO;

import com.Project.TalentConnect.entity.GigStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class GigRequestDto {

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @NotNull(message = "Price is Required")
    @Positive(message = "Price must be greater than 0")
    private Double price;

    @NotBlank(message = "Category cannot be empty")
    private String category;

    @NotNull(message = "Status is required")
    private GigStatus status;

    @NotNull(message = "Freelancer Id is required")
    private Long freelancerId;

}

