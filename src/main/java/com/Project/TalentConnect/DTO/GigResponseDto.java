package com.Project.TalentConnect.DTO;

import com.Project.TalentConnect.entity.GigStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GigResponseDto {

    private Long id;
    private String title;
    private String description;
    private double price;
    private String category;
    private GigStatus status;
    private Long freelancerId;
    private String freelancerName;
    private LocalDateTime createdAt;
    private Double averageRating;
}


