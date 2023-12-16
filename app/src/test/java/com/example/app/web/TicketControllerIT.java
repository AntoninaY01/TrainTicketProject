package com.example.app.web;

import com.example.app.entities.*;
import com.example.app.entities.enums.PassengerType;
import com.example.app.entities.enums.TicketStatus;
import com.example.app.repos.DestinationRepo;
import com.example.app.repos.TicketRepo;
import com.example.app.repos.UserRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TicketControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TicketRepo ticketRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private DestinationRepo destinationRepo;

    private Ticket ticket;

    private void createTicket(User user, Destination destination) {
        ticket = new Ticket();
        ticket.setUser(user);
        ticket.setWithChild(true);
        ticket.setPrice(BigDecimal.valueOf(50));
        ticket.setTime("12:00");
        ticket.setStatus(TicketStatus.CREATED);
        ticket.setDestinationList(Collections.singletonList(destination));
    }

    @BeforeEach
    void setup() {
        User user = new User();
        user.setFirstName("AA");
        user.setLastName("BB");
        user.setAge(22);
        user.setType(PassengerType.NONE);
        userRepo.saveAndFlush(user);

        Destination destination1 = new Destination();
        destination1.setName("CC");
        destinationRepo.saveAndFlush(destination1);

        createTicket(user, destination1);
    }

    @AfterEach
    void tearDown() {
        ticketRepo.deleteAll();
        userRepo.deleteAll();
        destinationRepo.deleteAll();
    }

    @Test
    void getAllTickets() throws Exception {
        ticketRepo.saveAndFlush(ticket);

        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[*].status").value("CREATED"));
    }

    @Test
    void getOneTicket() throws Exception {
        Ticket savedTicket = ticketRepo.saveAndFlush(ticket);

        mockMvc.perform(get("/api/tickets/" + savedTicket.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CREATED"));
    }

    @Test
    void cancelTicket() throws Exception {
        var savedTicket = ticketRepo.saveAndFlush(ticket);

        mockMvc.perform(put("/api/tickets/cancel/{id}", savedTicket.getId()))
                .andExpect(status().isIAmATeapot());

        Ticket canceledTicket = ticketRepo.findById(savedTicket.getId()).orElse(null);
        assertNotNull(canceledTicket);
        assertEquals(TicketStatus.CANCELED, canceledTicket.getStatus());
    }
}
