package com.example.app.controllers;

import com.example.app.services.BookingService;
import com.example.app.services.TicketService;
import com.example.app.services.dtos.BookingDTO;
import com.example.app.services.dtos.TicketDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketService ticketService;
    private final BookingService bookingService;

    public TicketController(TicketService ticketService, BookingService bookingService) {
        this.ticketService = ticketService;
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<Page<TicketDTO>> getAll(Pageable pageable) {
        final var tickets = ticketService.getAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(tickets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getOne(@PathVariable Long id) {
        final var ticket = ticketService.getOne(id);
        return ResponseEntity.status(HttpStatus.OK).body(ticket);
    }

    @PostMapping
    public ResponseEntity<TicketDTO> save(@RequestBody TicketDTO ticketDTO) {
        final var savedTicket = ticketService.save(ticketDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTicket);
    }

    @PutMapping
    public ResponseEntity<TicketDTO> update(@RequestBody TicketDTO ticketDTO) {
        final var updatedTicket = ticketService.update(ticketDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedTicket);
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        ticketService.cancel(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/checkPrice")
    public ResponseEntity<BigDecimal> getPrice(@RequestBody BookingDTO bookingDTO){
        return ResponseEntity.status(HttpStatus.OK).body(
                bookingService.calculatePrice(bookingDTO)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ticketService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
