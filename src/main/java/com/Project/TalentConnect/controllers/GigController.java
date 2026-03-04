package com.Project.TalentConnect.controllers;

import com.Project.TalentConnect.DTO.GigRequestDto;
import com.Project.TalentConnect.DTO.GigResponseDto;
import com.Project.TalentConnect.entity.GigEntity;
import com.Project.TalentConnect.services.GigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gigs")
@RequiredArgsConstructor
public class GigController {

    private final GigService gigService;

    //create gig
    @PostMapping("/freelancer/{freelancerId}")
    public GigResponseDto createGig(@PathVariable Long freelancerId,
                                    @RequestBody GigRequestDto gig){
        return gigService.createGig(freelancerId,gig);
    }

    //get all gigs
    @GetMapping
    public List<GigResponseDto> getAllGigs(){
        return gigService.getAllGigs();
    }

    //get gig by id
    @GetMapping("/{id}")
    public GigResponseDto getGigById(@PathVariable Long id){
        return gigService.getGigById(id);
    }

    //get gig by freelancer
    @GetMapping("/freelancer/{freelancerId}")
    public List<GigResponseDto> getGigsByFreelancer(@PathVariable Long freelancerId){
        return gigService.getGigsByFreelancer(freelancerId);
    }

    //get gig by category
    @GetMapping("/category/{category}")
    public List<GigResponseDto> getGigsByCategory(@PathVariable String category){
        return gigService.getGigsByCategory(category);
    }

    //delete gig
    @DeleteMapping("/{id}")
    public String deleteGig(@PathVariable Long id){
            gigService.deleteGig(id);
            return "Gig deleted Successfully";
        }
    }

