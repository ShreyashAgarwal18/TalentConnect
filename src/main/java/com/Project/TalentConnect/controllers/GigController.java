package com.Project.TalentConnect.controllers;

import com.Project.TalentConnect.DTO.GigRequestDto;
import com.Project.TalentConnect.DTO.GigResponseDto;
import com.Project.TalentConnect.services.GigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gigs")
@RequiredArgsConstructor
public class GigController {

    private final GigService gigService;

    //create gig
    @PostMapping
    public ResponseEntity<GigResponseDto> createGig(@Valid @RequestBody GigRequestDto request){
        GigResponseDto response = gigService.createGig(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //get all gigs
    @GetMapping
    public ResponseEntity<List<GigResponseDto>> getAllGigs(){

        List<GigResponseDto> gigs = gigService.getAllGigs();
        return ResponseEntity.ok(gigs);
    }

    //get gig by id
    @GetMapping("/{id}")
    public ResponseEntity<GigResponseDto> getGigById(@PathVariable Long id){

        GigResponseDto gig = gigService.getGigById(id);
        return ResponseEntity.ok(gig);
    }

    //get gig by freelancer
    @GetMapping("/freelancer/{freelancerId}")
    public ResponseEntity<List<GigResponseDto>> getGigsByFreelancer(@PathVariable Long freelancerId){
        List<GigResponseDto> gigs = gigService.getGigsByFreelancer(freelancerId);
        return ResponseEntity.ok(gigs);
    }

    //get gig by category
    @GetMapping("/category/{category}")
    public ResponseEntity<List<GigResponseDto>> getGigsByCategory(@PathVariable String category){
        List<GigResponseDto> gigs = gigService.getGigsByCategory(category);
        return ResponseEntity.ok(gigs);
    }

    //delete gig
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGig(@PathVariable Long id){
            gigService.deleteGig(id);
            return ResponseEntity.noContent().build();
        }
    }
