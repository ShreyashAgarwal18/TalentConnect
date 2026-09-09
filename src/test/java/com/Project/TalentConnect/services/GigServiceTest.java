package com.Project.TalentConnect.services;

import com.Project.TalentConnect.DTO.GigRequestDto;
import com.Project.TalentConnect.entity.GigEntity;
import com.Project.TalentConnect.entity.UserEntity;
import com.Project.TalentConnect.exception.BadRequestException;
import com.Project.TalentConnect.exception.ResourceNotFoundException;
import com.Project.TalentConnect.repository.GigRepository;
import com.Project.TalentConnect.repository.ReviewRepository;
import com.Project.TalentConnect.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GigServiceTest {

    @Mock private GigRepository gigRepository;
    @Mock private UserRepository userRepository;
    @Mock private ModelMapper modelMapper;
    @Mock private ReviewRepository reviewRepository;

    @InjectMocks private GigService gigService;

    @Test
    void createGig_ShouldThrow_WhenFreelancerNotFound() {
        GigRequestDto request = new GigRequestDto();

        when(userRepository.findByEmail("ghost@test.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> gigService.createGig(request, "ghost@test.com"));
        verify(gigRepository, never()).save(any());
    }

    @Test
    void createGig_ShouldAssignFreelancer_WhenFreelancerExists() {
        GigRequestDto request = new GigRequestDto();
        UserEntity freelancer = UserEntity.builder().id(1L).email("freelancer@test.com").build();
        GigEntity mappedGig = new GigEntity();

        when(userRepository.findByEmail("freelancer@test.com")).thenReturn(Optional.of(freelancer));
        when(modelMapper.map(request, GigEntity.class)).thenReturn(mappedGig);
        when(gigRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(reviewRepository.findAverageRatingByGigId(any())).thenReturn(null);

        gigService.createGig(request, "freelancer@test.com");

        assertEquals(freelancer, mappedGig.getFreelancer());
        verify(gigRepository).save(mappedGig);
    }

    @Test
    void getGigById_ShouldThrow_WhenNotFound() {
        when(gigRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> gigService.getGigById(99L));
    }

    @Test
    void getGigsByFreelancer_ShouldThrow_WhenFreelancerNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> gigService.getGigsByFreelancer(99L));
    }

    @Test
    void deleteGig_ShouldThrow_WhenGigNotFound() {
        when(gigRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> gigService.deleteGig(99L, "anyone@test.com"));
        verify(gigRepository, never()).delete(any());
    }

    @Test
    void deleteGig_ShouldThrow_WhenCallerIsNotOwner() {
        UserEntity owner = UserEntity.builder().email("owner@test.com").build();
        GigEntity gig = GigEntity.builder().freelancer(owner).build();

        when(gigRepository.findById(1L)).thenReturn(Optional.of(gig));

        assertThrows(BadRequestException.class,
                () -> gigService.deleteGig(1L, "hacker@test.com"));
        verify(gigRepository, never()).delete(any());
    }

    @Test
    void deleteGig_ShouldSucceed_WhenCallerIsOwner() {
        UserEntity owner = UserEntity.builder().email("owner@test.com").build();
        GigEntity gig = GigEntity.builder().freelancer(owner).build();

        when(gigRepository.findById(1L)).thenReturn(Optional.of(gig));

        gigService.deleteGig(1L, "owner@test.com");

        verify(gigRepository).delete(gig);
    }
}
