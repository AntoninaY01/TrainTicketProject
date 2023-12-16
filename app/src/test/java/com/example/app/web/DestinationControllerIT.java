package com.example.app.web;

import com.example.app.entities.Destination;
import com.example.app.repos.DestinationRepo;
import com.example.app.services.dtos.DestinationDTO;
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

@AutoConfigureMockMvc
@SpringBootTest
class DestinationControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DestinationRepo destinationRepo;

    @Autowired
    private ObjectMapper objectMapper;

    private Destination destination;

    private void createDestination() {
        destination = new Destination();
        destination.setName("Sofia");
    }

    @BeforeEach
    void setup() {
        createDestination();
    }

    @AfterEach
    void tearDown() {
        destinationRepo.deleteAll();
    }

    @Test
    void getAll() throws Exception {
        destinationRepo.saveAndFlush(destination);

        mockMvc.perform(get("/api/destinations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[*].name").value("Sofia"));
    }

    @Test
    void getOne() throws Exception {
        Destination savedDestination = destinationRepo.saveAndFlush(destination);

        mockMvc.perform(get("/api/destinations/" + savedDestination.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sofia"));
    }

    @Test
    void saveDestination() throws Exception {
        DestinationDTO destinationDTO = new DestinationDTO();
        destinationDTO.setName("New Destination");

        String jsonRequest = objectMapper.writeValueAsString(destinationDTO);

        mockMvc.perform(post("/api/destinations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Destination"));
    }

    @Test
    void updateDestination() throws Exception {
        var savedDestination = destinationRepo.saveAndFlush(destination);

        DestinationDTO existingDestinationDTO = new DestinationDTO();
        existingDestinationDTO.setId(savedDestination.getId());
        existingDestinationDTO.setName("Updated Destination");

        String jsonRequest = objectMapper.writeValueAsString(existingDestinationDTO);

        mockMvc.perform(put("/api/destinations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Destination"));
    }

    @Test
    void deleteDestination() throws Exception {
        var savedDestination = destinationRepo.saveAndFlush(destination);

        mockMvc.perform(delete("/api/destinations/{id}", savedDestination.getId()))
                .andExpect(status().isNoContent());

        assertFalse(destinationRepo.existsById(1L));
    }
}
