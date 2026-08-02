package com.Project.TalentConnect.DTO;


import java.time.LocalDateTime;
import lombok.*;

@Data
@Builder
public class ReviewResponseDto {
    
    private Long id;
    private Long orderId;
    private Long gigId;
    private String gigTitle;
    private Long reviewerId;
    private String reviewerName;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;

}
