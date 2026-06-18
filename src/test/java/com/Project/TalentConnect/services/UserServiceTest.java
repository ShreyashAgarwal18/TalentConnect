package com.Project.TalentConnect.services;

import com.Project.TalentConnect.DTO.UserRequestDto;
import com.Project.TalentConnect.DTO.UserResponseDto;
import com.Project.TalentConnect.entity.UserEntity;
import com.Project.TalentConnect.exception.BadRequestException;
import com.Project.TalentConnect.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private ModelMapper modelMapper;

    @InjectMocks private UserService userService;

    @Test
    void registerUser_ShouldThrow_WhenEmailAlreadyExists() {
        UserRequestDto request = new UserRequestDto();
        request.setEmail("existing@test.com");

        when(userRepository.existsByEmail("existing@test.com")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> userService.registerUser(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerUser_ShouldEncodePassword() {
        UserRequestDto request = new UserRequestDto();
        request.setEmail("new@test.com");
        request.setPassword("plaintext");

        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode("plaintext")).thenReturn("encoded");
        when(modelMapper.map(any(), eq(UserEntity.class))).thenReturn(new UserEntity());
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(modelMapper.map(any(), eq(UserResponseDto.class))).thenReturn(new UserResponseDto());

        userService.registerUser(request);

        verify(passwordEncoder).encode("plaintext");
    }

    @Test
    void deleteUser_ShouldThrow_WhenUserNotFound() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThrows(Exception.class, () -> userService.deleteUser(99L));
        verify(userRepository, never()).deleteById(any());
    }
}
