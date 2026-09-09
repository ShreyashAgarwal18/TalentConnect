package com.Project.TalentConnect.services;

import com.Project.TalentConnect.DTO.UserRequestDto;
import com.Project.TalentConnect.DTO.UserResponseDto;
import com.Project.TalentConnect.entity.UserEntity;
import com.Project.TalentConnect.exception.BadRequestException;
import com.Project.TalentConnect.exception.ResourceNotFoundException;
import com.Project.TalentConnect.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

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
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(99L, "caller@test.com"));
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void deleteUser_ShouldSucceed_WhenCallerDeletesSelf() {
        UserEntity self = UserEntity.builder().id(1L).email("self@test.com").role(com.Project.TalentConnect.entity.Role.CLIENT).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(self));
        when(userRepository.findByEmail("self@test.com")).thenReturn(Optional.of(self));

        userService.deleteUser(1L, "self@test.com");

        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_ShouldThrow_WhenCallerIsNeitherOwnerNorAdmin() {
        UserEntity target = UserEntity.builder().id(1L).email("target@test.com").role(com.Project.TalentConnect.entity.Role.CLIENT).build();
        UserEntity caller = UserEntity.builder().id(2L).email("caller@test.com").role(com.Project.TalentConnect.entity.Role.CLIENT).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(target));
        when(userRepository.findByEmail("caller@test.com")).thenReturn(Optional.of(caller));

        assertThrows(BadRequestException.class, () -> userService.deleteUser(1L, "caller@test.com"));
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void deleteUser_ShouldSucceed_WhenCallerIsAdmin() {
        UserEntity target = UserEntity.builder().id(1L).email("target@test.com").role(com.Project.TalentConnect.entity.Role.CLIENT).build();
        UserEntity admin = UserEntity.builder().id(2L).email("admin@test.com").role(com.Project.TalentConnect.entity.Role.ADMIN).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(target));
        when(userRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(admin));

        userService.deleteUser(1L, "admin@test.com");

        verify(userRepository).deleteById(1L);
    }
}
