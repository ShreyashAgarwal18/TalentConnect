package com.Project.TalentConnect.services;

import com.Project.TalentConnect.DTO.GigRequestDto;
import com.Project.TalentConnect.DTO.GigResponseDto;
import com.Project.TalentConnect.entity.GigEntity;
import com.Project.TalentConnect.entity.UserEntity;
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
    public GigResponseDto createGig(Long freelancerId, GigRequestDto request){

        UserEntity freelancer = userRepository.findById(freelancerId)
                .orElseThrow(() -> new RuntimeException("Freelancer not found"));

        GigEntity gig = modelMapper.map(request, GigEntity.class);

        gig.setFreelancer(freelancer);
        gig.setCreatedAt(LocalDateTime.now());

        GigEntity savedGig = gigRepository.save(gig);

        return maptoResponse(savedGig);
    }

    //get all gigs
    public List<GigResponseDto> getAllGigs(){
        return gigRepository.findAll()
                .stream()
                .map(this::maptoResponse)
                .toList();
    }

    //get gig by id
    public GigResponseDto getGigById(Long gigId){
        GigEntity gig = gigRepository.findById(gigId)
                .orElseThrow(() -> new RuntimeException("Gig Not Found"));

        return maptoResponse(gig);
    }

    //get gig by Freelancer
    public List<GigResponseDto> getGigsByFreelancer(Long freelancerId){
        UserEntity freelancer = userRepository.findById(freelancerId)
                .orElseThrow(() -> new RuntimeException("Freelancer not found"));

        return gigRepository.findByFreelancer(freelancer)
                .stream()
                .map(this::maptoResponse)
                .toList();
    }

    //get gig by category
    public List<GigResponseDto> getGigsByCategory(String category){
        return gigRepository.findByCategory(category)
                .stream()
                .map(this::maptoResponse)
                .toList();
    }

    //delete a gig
    public void deleteGig(Long gigId){
        if(!gigRepository.existsById(gigId)){
            throw new RuntimeException("Gig not found");
        }
        gigRepository.deleteById(gigId);
    }

    private GigResponseDto maptoResponse(GigEntity gig){

        return GigResponseDto.builder()
                .id(gig.getId())
                .title(gig.getTitle())
                .description(gig.getDescription())
                .price(gig.getPrice())
                .category(gig.getCategory())
                .status(gig.getStatus())
                .freelancerId(gig.getFreelancer().getId())
                .freelancerName(gig.getFreelancer().getName())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
