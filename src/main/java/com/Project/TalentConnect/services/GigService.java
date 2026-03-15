package com.Project.TalentConnect.services;

import com.Project.TalentConnect.DTO.GigRequestDto;
import com.Project.TalentConnect.DTO.GigResponseDto;
import com.Project.TalentConnect.entity.GigEntity;
import com.Project.TalentConnect.entity.UserEntity;
import com.Project.TalentConnect.exception.ResourceNotFoundException;
import com.Project.TalentConnect.repository.GigRepository;
import com.Project.TalentConnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GigService {

    private final GigRepository gigRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    //create gig
    public GigResponseDto createGig(GigRequestDto request){

        UserEntity freelancer = userRepository.findById(request.getFreelancerId())
                .orElseThrow(() -> new ResourceNotFoundException("Freelancer not found with id: " + request.getFreelancerId()));

        GigEntity gig = modelMapper.map(request, GigEntity.class);

        gig.setFreelancer(freelancer);


        GigEntity savedGig = gigRepository.save(gig);

        return mapToResponse(savedGig);
    }

    //get all gigs
    public List<GigResponseDto> getAllGigs(){
        return gigRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    //get gig by id
    public GigResponseDto getGigById(Long gigId){
        GigEntity gig = gigRepository.findById(gigId)
                .orElseThrow(() -> new ResourceNotFoundException("Gig not found with id: " + gigId));

        return mapToResponse(gig);
    }

    //get gig by freelancer
    public List<GigResponseDto> getGigsByFreelancer(Long freelancerId){
        UserEntity freelancer = userRepository.findById(freelancerId)
                .orElseThrow(() -> new ResourceNotFoundException("Freelancer not found with id: " + freelancerId));

        return gigRepository.findByFreelancer(freelancer)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    //get gig by category
    public List<GigResponseDto> getGigsByCategory(String category){
        return gigRepository.findByCategory(category)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    //delete a gig
    public void deleteGig(Long gigId){
       if(!gigRepository.existsById(gigId)){
           throw new ResourceNotFoundException("Gig not found with id: " + gigId);
       }

       gigRepository.deleteById(gigId);
    }

    private GigResponseDto mapToResponse(GigEntity gig){

        return GigResponseDto.builder()
                .id(gig.getId())
                .title(gig.getTitle())
                .description(gig.getDescription())
                .price(gig.getPrice())
                .category(gig.getCategory())
                .status(gig.getStatus())
                .freelancerId(gig.getFreelancer().getId())
                .freelancerName(gig.getFreelancer().getName())
                .createdAt(gig.getCreatedAt())
                .build();
    }
}

