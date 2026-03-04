package com.Project.TalentConnect.DTO;

import com.Project.TalentConnect.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private String phone;
    private Boolean enabled;
    private LocalDateTime createdAt;
}
