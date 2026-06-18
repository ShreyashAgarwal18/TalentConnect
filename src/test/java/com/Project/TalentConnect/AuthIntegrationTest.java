package com.Project.TalentConnect;

import com.Project.TalentConnect.DTO.AuthRequestDto;
import com.Project.TalentConnect.DTO.UserRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.Project.TalentConnect.entity.Role;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void register_ThenLogin_ShouldReturnTokens() throws Exception {
        UserRequestDto register = new UserRequestDto();
        register.setName("Test User");
        register.setEmail("testuser@test.com");
        register.setPassword("password123");
        register.setPhone("9876543210");
        register.setRole(Role.CLIENT);

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isCreated());

        AuthRequestDto login = new AuthRequestDto();
        login.setEmail("testuser@test.com");
        login.setPassword("password123");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    void protectedEndpoint_ShouldReturn401_WithNoToken() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void publicEndpoint_ShouldReturn200_WithNoToken() throws Exception {
        mockMvc.perform(get("/api/gigs"))
                .andExpect(status().isOk());
    }
}
