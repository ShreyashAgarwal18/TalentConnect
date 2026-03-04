package com.Project.TalentConnect.DTO;

import com.Project.TalentConnect.entity.GigStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
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
}


