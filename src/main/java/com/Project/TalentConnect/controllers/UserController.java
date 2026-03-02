package com.Project.TalentConnect.controllers;

import com.Project.TalentConnect.entity.UserEntity;
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
    public UserEntity registerUser(@RequestBody UserEntity user){
        return userService.registerUser(user);
    }

    //get user by id
    @GetMapping("/{id}")
    public UserEntity getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    //get all users
    @GetMapping
    public List<UserEntity> getAllUsers(){
        return userService.getAllUsers();
    }

    //delete User
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return "User Deleted Successfully";
    }
}


