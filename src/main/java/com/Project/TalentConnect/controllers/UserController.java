package com.Project.TalentConnect.controllers;

import com.Project.TalentConnect.DTO.UserRequestDto;
import com.Project.TalentConnect.DTO.UserResponseDto;
import com.Project.TalentConnect.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //register new user
    @PostMapping("/register")
    public UserResponseDto registerUser(@RequestBody UserRequestDto request){
        return userService.registerUser(request);
    }

    //get user by id
    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    //get all users
    @GetMapping
    public List<UserResponseDto> getAllUsers(){
        return userService.getAllUsers();
    }

    //delete User
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return "User Deleted Successfully";
    }
}


