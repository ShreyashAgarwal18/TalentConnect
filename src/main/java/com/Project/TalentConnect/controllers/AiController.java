package com.Project.TalentConnect.controllers;

import com.Project.TalentConnect.DTO.GigDescriptionRequestDto;
import com.Project.TalentConnect.DTO.GigDescriptionResponseDto;
import com.Project.TalentConnect.services.AiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/generate-description")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<GigDescriptionResponseDto> generateDescription(
            @Valid @RequestBody GigDescriptionRequestDto request) {
        return ResponseEntity.ok(aiService.generateDescription(request));
    }
}
