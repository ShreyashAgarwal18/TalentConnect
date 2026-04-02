package com.Project.TalentConnect.controllers;

import com.Project.TalentConnect.DTO.UserRequestDto;
import com.Project.TalentConnect.DTO.UserResponseDto;
import com.Project.TalentConnect.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //register new user
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@Valid @RequestBody UserRequestDto request){
        UserResponseDto response = userService.registerUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //get user by id
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser(Authentication authentication){
        String email = authentication.getName();
        UserResponseDto response = userService.getUserByEmail(email);
        return ResponseEntity.ok(response);
    }

    //get all users
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers(){

        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    //delete User
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}


