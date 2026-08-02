package com.Project.TalentConnect.services;

import com.Project.TalentConnect.DTO.ReviewRequestDto;
import com.Project.TalentConnect.DTO.ReviewResponseDto;
import com.Project.TalentConnect.entity.GigEntity;
import com.Project.TalentConnect.entity.OrderEntity;
import com.Project.TalentConnect.entity.OrderStatus;
import com.Project.TalentConnect.entity.ReviewEntity;
import com.Project.TalentConnect.entity.UserEntity;
import com.Project.TalentConnect.exception.BadRequestException;
import com.Project.TalentConnect.exception.ResourceNotFoundException;
import com.Project.TalentConnect.repository.GigRepository;
import com.Project.TalentConnect.repository.OrderRepository;
import com.Project.TalentConnect.repository.ReviewRepository;
import com.Project.TalentConnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final GigRepository gigRepository;

    
    @Transactional
    public ReviewResponseDto createReview(ReviewRequestDto dto, String reviewerEmail){
        OrderEntity order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + dto.getOrderId()));
    

    if(!order.getClient().getEmail().equals(reviewerEmail)){
        throw new BadRequestException("You can only review orders you placed");
    }

    if(order.getStatus() != OrderStatus.COMPLETED){
        throw new BadRequestException("You can only review completed orders");
    }

    if(reviewRepository.existsByOrderId(dto.getOrderId())){
        throw new BadRequestException("You have already reviewed this order");
    }

    UserEntity reviewer = userRepository.findByEmail(reviewerEmail)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));


    GigEntity gig = order.getGig();

    ReviewEntity review = ReviewEntity.builder()
                            .order(order)
                            .reviewer(reviewer)
                            .gig(gig)
                            .rating(dto.getRating())
                            .comment(dto.getComment())
                            .build();

    return mapToResponse(reviewRepository.save(review));
    }

    public List<ReviewResponseDto> getReviewsByGig(Long gigId){
    if(!gigRepository.existsById(gigId)){
        throw new ResourceNotFoundException("Gig not found with id: " + gigId);
    }

    return reviewRepository.findByGigId(gigId)
            .stream()
            .map(this::mapToResponse)
            .toList();
}


public List<ReviewResponseDto> getReviewsByFreelancer(Long freelancerId){
    if(!userRepository.existsById(freelancerId)){
        throw new ResourceNotFoundException("User not found with id: " + freelancerId);
    }
    return reviewRepository.findByFreelancerId(freelancerId)
            .stream()
            .map(this::mapToResponse)
            .toList();
}

private ReviewResponseDto mapToResponse(ReviewEntity review){
    return ReviewResponseDto.builder()
                .id(review.getId())
                .orderId(review.getOrder().getId())
                .gigId(review.getGig().getId())
                .gigTitle(review.getGig().getTitle())
                .reviewerId(review.getReviewer().getId())
                .reviewerName(review.getReviewer().getName())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
}

}





