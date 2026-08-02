package com.Project.TalentConnect.controllers;

import com.Project.TalentConnect.DTO.ReviewRequestDto;
import com.Project.TalentConnect.DTO.ReviewResponseDto;
import com.Project.TalentConnect.services.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    
    private final ReviewService reviewService;

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ReviewResponseDto> createReview(
        @Valid @RequestBody ReviewRequestDto dto,
        Authentication authentication){
            return ResponseEntity.status(HttpStatus.CREATED)
                        .body(reviewService.createReview(dto, authentication.getName()));
        }

    @GetMapping("/gig/{gigId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByGig(@PathVariable Long gigId){
        return ResponseEntity.ok(reviewService.getReviewsByGig(gigId));
    }

    @GetMapping("/freelancer/{freelancerId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByFreelancer(@PathVariable Long freelancerId){
        return ResponseEntity.ok(reviewService.getReviewsByGig(freelancerId));
    }
}
