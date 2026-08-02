package com.Project.TalentConnect.DTO;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderRequestDto {

    @NotNull(message = "Gig id is required")
    private Long gigId;

    
 

    private LocalDateTime deadline;
}




