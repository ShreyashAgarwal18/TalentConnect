package com.Project.TalentConnect.DTO;

import com.Project.TalentConnect.entity.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRequestDto {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @Size(min = 6, message = "Password must atleast have 6 characters")
    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotNull(message = "Role is required")
    private Role role;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phone;
}


