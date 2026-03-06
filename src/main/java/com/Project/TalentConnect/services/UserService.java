package com.Project.TalentConnect.services;

import com.Project.TalentConnect.DTO.UserRequestDto;
import com.Project.TalentConnect.DTO.UserResponseDto;
import com.Project.TalentConnect.entity.UserEntity;
import com.Project.TalentConnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.Project.TalentConnect.exception.ResourceNotFoundException;
import com.Project.TalentConnect.exception.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    //register new user
    public UserResponseDto registerUser(UserRequestDto request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new BadRequestException("Email already exists");
        }
      UserEntity user = modelMapper.map(request, UserEntity.class);

      user.setEnabled(true);
      user.setCreatedAt(LocalDateTime.now());

      UserEntity savedUser = userRepository.save(user);

      return modelMapper.map(savedUser, UserResponseDto.class);
    }

    //get user by id
    public UserResponseDto getUserById(Long id){
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        return modelMapper.map(user, UserResponseDto.class);
    }

    //get all users
    public List<UserResponseDto> getAllUsers(){

        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserResponseDto.class))
                .toList();
    }

    //delete User
    public void deleteUser(Long id){
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        userRepository.delete(user);
    }

    //find user by email
    public Optional<UserEntity> findByEmail(String email){

        return userRepository.findByEmail(email);
    }
}


