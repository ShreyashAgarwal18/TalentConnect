package com.Project.TalentConnect.services;

import com.Project.TalentConnect.DTO.GigRequestDto;
import com.Project.TalentConnect.DTO.GigResponseDto;
import com.Project.TalentConnect.entity.GigEntity;
import com.Project.TalentConnect.entity.UserEntity;
import com.Project.TalentConnect.exception.BadRequestException;
import com.Project.TalentConnect.exception.ResourceNotFoundException;
import com.Project.TalentConnect.repository.GigRepository;
import com.Project.TalentConnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GigService {

    private final GigRepository gigRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    //create gig
    @Transactional
    public GigResponseDto createGig(GigRequestDto request, String email){

        UserEntity freelancer = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        GigEntity gig = modelMapper.map(request, GigEntity.class);

        gig.setId(null);
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
    @Transactional
    public void deleteGig(Long gigId, String callerEmail){

        GigEntity gig = gigRepository.findById(gigId)
                .orElseThrow(() -> new ResourceNotFoundException("Gig not found with id: " + gigId));

        if(!gig.getFreelancer().getEmail().equals(callerEmail)){
            throw new BadRequestException("You are not authorized to delete this gig");
        }
        gigRepository.delete(gig);
    }

    public Page<GigResponseDto> getAllGigsPaginated(int page, int size, String sortBy, String direction){

        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<GigEntity> gigPage = gigRepository.findAll(pageable);

        return gigPage.map(this::mapToResponse);
    }


    public List<GigResponseDto> search(String keyword){
        return gigRepository.search(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
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

