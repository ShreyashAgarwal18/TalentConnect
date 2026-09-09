package com.Project.TalentConnect.services;

import com.Project.TalentConnect.DTO.ReviewRequestDto;
import com.Project.TalentConnect.entity.GigEntity;
import com.Project.TalentConnect.entity.OrderEntity;
import com.Project.TalentConnect.entity.OrderStatus;
import com.Project.TalentConnect.entity.UserEntity;
import com.Project.TalentConnect.exception.BadRequestException;
import com.Project.TalentConnect.exception.ResourceNotFoundException;
import com.Project.TalentConnect.repository.GigRepository;
import com.Project.TalentConnect.repository.OrderRepository;
import com.Project.TalentConnect.repository.ReviewRepository;
import com.Project.TalentConnect.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock private ReviewRepository reviewRepository;
    @Mock private OrderRepository orderRepository;
    @Mock private UserRepository userRepository;
    @Mock private GigRepository gigRepository;

    @InjectMocks private ReviewService reviewService;

    private ReviewRequestDto requestFor(long orderId) {
        ReviewRequestDto dto = new ReviewRequestDto();
        dto.setOrderId(orderId);
        dto.setRating(5);
        dto.setComment("Great work");
        return dto;
    }

    @Test
    void createReview_ShouldThrow_WhenOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> reviewService.createReview(requestFor(1L), "client@test.com"));
    }

    @Test
    void createReview_ShouldThrow_WhenCallerIsNotOrderClient() {
        UserEntity client = UserEntity.builder().email("owner@test.com").build();
        OrderEntity order = OrderEntity.builder().client(client).status(OrderStatus.COMPLETED).build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(BadRequestException.class,
                () -> reviewService.createReview(requestFor(1L), "hacker@test.com"));
        verify(reviewRepository, never()).save(any());
    }

    @Test
    void createReview_ShouldThrow_WhenOrderNotCompleted() {
        UserEntity client = UserEntity.builder().email("client@test.com").build();
        OrderEntity order = OrderEntity.builder().client(client).status(OrderStatus.IN_PROGRESS).build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(BadRequestException.class,
                () -> reviewService.createReview(requestFor(1L), "client@test.com"));
        verify(reviewRepository, never()).save(any());
    }

    @Test
    void createReview_ShouldThrow_WhenOrderAlreadyReviewed() {
        UserEntity client = UserEntity.builder().email("client@test.com").build();
        OrderEntity order = OrderEntity.builder().client(client).status(OrderStatus.COMPLETED).build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(reviewRepository.existsByOrderId(1L)).thenReturn(true);

        assertThrows(BadRequestException.class,
                () -> reviewService.createReview(requestFor(1L), "client@test.com"));
        verify(reviewRepository, never()).save(any());
    }

    @Test
    void createReview_ShouldSucceed_WhenOrderIsCompletedAndUnreviewed() {
        UserEntity client = UserEntity.builder().id(1L).email("client@test.com").name("Client").build();
        GigEntity gig = GigEntity.builder().id(2L).title("Logo Design").build();
        OrderEntity order = OrderEntity.builder().id(1L).client(client).gig(gig).status(OrderStatus.COMPLETED).build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(reviewRepository.existsByOrderId(1L)).thenReturn(false);
        when(userRepository.findByEmail("client@test.com")).thenReturn(Optional.of(client));
        when(reviewRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        var response = reviewService.createReview(requestFor(1L), "client@test.com");

        assertEquals(5, response.getRating());
        assertEquals(2L, response.getGigId());
        verify(reviewRepository).save(any());
    }

    @Test
    void getReviewsByGig_ShouldThrow_WhenGigNotFound() {
        when(gigRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> reviewService.getReviewsByGig(99L));
    }

    @Test
    void getReviewsByFreelancer_ShouldThrow_WhenFreelancerNotFound() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> reviewService.getReviewsByFreelancer(99L));
    }
}
