package com.Project.TalentConnect.services;

import com.Project.TalentConnect.DTO.UserRequestDto;
import com.Project.TalentConnect.DTO.UserResponseDto;
import com.Project.TalentConnect.entity.Role;
import com.Project.TalentConnect.entity.UserEntity;
import com.Project.TalentConnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.Project.TalentConnect.exception.ResourceNotFoundException;
import com.Project.TalentConnect.exception.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {


    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    //register new user
    public UserResponseDto registerUser(UserRequestDto request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new BadRequestException("Email already exists");
        }
      UserEntity user = modelMapper.map(request, UserEntity.class);

      user.setPassword(passwordEncoder.encode(request.getPassword()));
      user.setEnabled(true);

        if (request.getRole() == null) {
            user.setRole(Role.CLIENT);
        } else {
            user.setRole(request.getRole());
        }

      UserEntity savedUser = userRepository.save(user);

      return modelMapper.map(savedUser, UserResponseDto.class);
    }

    //get user by id
    public UserResponseDto getUserById(Long id){
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        return modelMapper.map(user, UserResponseDto.class);
    }

    //get user by email
    public UserResponseDto getUserByEmail(String email){

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

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

        if(!userRepository.existsById(id)){
            throw new ResourceNotFoundException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
    }

    //find user by email
    public UserEntity findUserEntityByEmail(String email){

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }
}





