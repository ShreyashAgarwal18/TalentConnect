package com.Project.TalentConnect.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReviewRequestDto {
    
    @NotNull(message = "Order ID id required")
    private Long orderId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be atleast 1")
    @Max(value = 5, message = "Rating must at most  be 5")
    private Integer rating;

    @Size(max = 500, message = "Comment cannot exceed 500 characters")
    private String comment;
    
}
