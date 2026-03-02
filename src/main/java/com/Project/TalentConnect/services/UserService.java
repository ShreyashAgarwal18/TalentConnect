package com.Project.TalentConnect.services;

import com.Project.TalentConnect.entity.UserEntity;
import com.Project.TalentConnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    //register new user
    public UserEntity registerUser(UserEntity user){
        if(userRepository.existsByEmail(user.getEmail())){
            throw new RuntimeException("Email already exists");
        }
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    //get user by id
    public UserEntity getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    //get all users
    public List<UserEntity> getAllUsers(){
        return userRepository.findAll();
    }

    //delete User
    public void deleteUser(Long id){
        if(!userRepository.existsById(id)){
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    //find user by email
    public Optional<UserEntity> findByEmail(String email){
        return userRepository.findByEmail(email);
    }
}
