package com.Project.TalentConnect.DTO;

import com.Project.TalentConnect.entity.Role;
import lombok.Data;

@Data
public class UserRequestDto {
    private String name;
    private String email;
    private String password;
    private Role role;
    private String phone;
}


