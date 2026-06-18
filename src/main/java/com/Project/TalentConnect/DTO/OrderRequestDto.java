package com.Project.TalentConnect.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderRequestDto {

    @NotNull(message = "Gig id is required")
    private Long gigId;

    
    @NotNull(message = "Total amount is required")
    @Positive(message = "Total amount must be greater than 0")
    private Double totalAmount;

    private LocalDateTime deadline;
}




