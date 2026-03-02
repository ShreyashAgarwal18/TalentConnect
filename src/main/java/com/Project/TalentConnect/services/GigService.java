package com.Project.TalentConnect.services;

import com.Project.TalentConnect.entity.GigEntity;
import com.Project.TalentConnect.entity.UserEntity;
import com.Project.TalentConnect.repository.GigRepository;
import com.Project.TalentConnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GigService {

    private final GigRepository gigRepository;
    private final UserRepository userRepository;

    //create gig
    public GigEntity createGig(Long freelancerId, GigEntity gig){

        UserEntity freelancer = userRepository.findById(freelancerId)
                .orElseThrow(() -> new RuntimeException("Freelancer not found"));

        gig.setFreelancer(freelancer);

        gig.setCreatedAt(LocalDateTime.now());

        return gigRepository.save(gig);
    }

    //get all gigs
    public List<GigEntity> getAllGigs(){
        return gigRepository.findAll();
    }

    //get gig by id
    public GigEntity getGigById(Long gigId){
        return gigRepository.findById(gigId)
                .orElseThrow(() -> new RuntimeException("Gig Not Found"));
    }

    //get gig by Freelancer
    public List<GigEntity> getGigsByFreelancer(Long freelancerId){
        UserEntity freelancer = userRepository.findById(freelancerId)
                .orElseThrow(() -> new RuntimeException("Freelancer not found"));

        return gigRepository.findByFreelancer(freelancer);
    }

    //get gig by category
    public List<GigEntity> getGigsByCategory(String category){
        return gigRepository.findByCategory(category);
    }

    //delete a gig
    public void deleteGig(Long gigId){
        if(!gigRepository.existsById(gigId)){
            throw new RuntimeException("Gig not found");
        }
        gigRepository.deleteById(gigId);
    }
}
