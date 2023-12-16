package com.example.app.web;

import com.example.app.entities.enums.PassengerType;
import com.example.app.entities.User;
import com.example.app.repos.UserRepo;
import com.example.app.services.dtos.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    private void createUser() {
        user = new User();
        user.setFirstName("john");
        user.setLastName("cena");
        user.setAge(12);
        user.setType(PassengerType.NONE);
    }

    @BeforeEach
    void setup() {
        userRepo.deleteAll();
        createUser();
    }

    @AfterEach
    void tearDown() {
        userRepo.deleteAll();
    }

    @Test
    void getAllUsers() throws Exception {
        userRepo.saveAndFlush(user);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[*].firstName").value("john"));
    }

    @Test
    void getOneUser() throws Exception {
        User savedUser = userRepo.saveAndFlush(user);

        mockMvc.perform(get("/api/users/" + savedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("john"));
    }

    @Test
    void saveUser() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("john");
        userDTO.setLastName("cena");
        userDTO.setAge(12);
        userDTO.setType(PassengerType.NONE.toString());

        String jsonRequest = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("john"));
    }

    @Test
    void updateUser() throws Exception {
        var savedUser = userRepo.saveAndFlush(user);

        UserDTO existingUserDTO = new UserDTO();
        existingUserDTO.setId(savedUser.getId());
        existingUserDTO.setFirstName("updated_user");

        String jsonRequest = objectMapper.writeValueAsString(existingUserDTO);

        mockMvc.perform(put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("updated_user"));
    }

    @Test
    void deleteUser() throws Exception {
        var savedUser = userRepo.saveAndFlush(user);

        mockMvc.perform(delete("/api/users/{id}", savedUser.getId()))
                .andExpect(status().isNoContent());

        assertFalse(userRepo.existsById(savedUser.getId()));
    }
}
