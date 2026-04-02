package com.Project.TalentConnect.controllers;


import com.Project.TalentConnect.DTO.AuthRequestDto;
import com.Project.TalentConnect.DTO.AuthResponseDto;
import com.Project.TalentConnect.DTO.RefreshTokenRequestDto;
import com.Project.TalentConnect.entity.RefreshToken;
import com.Project.TalentConnect.security.JwtUtil;
import com.Project.TalentConnect.services.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto request){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
       String accessToken = jwtUtil.generateToken(request.getEmail());

       RefreshToken refreshToken = refreshTokenService.createRefreshToken(request.getEmail());


        return ResponseEntity.ok(
                new AuthResponseDto(accessToken, refreshToken.getToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refreshToken(@RequestBody RefreshTokenRequestDto request){
        RefreshToken refreshToken = refreshTokenService.findByToken(request.getRefreshToken());

        refreshTokenService.verifyExpiration(refreshToken);

        String newAccessToken = jwtUtil.generateToken(
                refreshToken.getUser().getEmail()
        );

        return ResponseEntity.ok(
                new AuthResponseDto(newAccessToken, request.getRefreshToken())
        );
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(Authentication authentication){

        String email = authentication.getName();

        refreshTokenService.deleteByUserEmail(email);

        return ResponseEntity.ok("Logged out successfully");
    }
}

