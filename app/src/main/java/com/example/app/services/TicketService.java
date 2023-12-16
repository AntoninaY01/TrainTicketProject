package com.example.app.services;

import com.example.app.entities.Destination;
import com.example.app.entities.Ticket;
import com.example.app.entities.enums.TicketStatus;
import com.example.app.repos.TicketRepo;
import com.example.app.services.dtos.BookingDTO;
import com.example.app.services.dtos.TicketDTO;
import com.example.app.services.mappers.TicketMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalTime;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class TicketService {
    private final BookingService bookingService;
    private final TicketRepo ticketRepo;
    private final TicketMapper ticketMapper;

    public TicketService(BookingService bookingService, TicketRepo ticketRepo, TicketMapper ticketMapper) {
        this.bookingService = bookingService;
        this.ticketRepo = ticketRepo;
        this.ticketMapper = ticketMapper;
    }

    public Page<TicketDTO> getAll(Pageable pageable) {
        log.debug("Request to get all TICKETS");
        return ticketRepo.findAll(pageable).map(ticketMapper::toDTO);
    }

    public TicketDTO getOne(Long id) {
        log.debug("Request to get TICKET by ID: {}", id);
        return ticketMapper.toDTO(ticketRepo.findById(id).orElse(null));
    }

    public TicketDTO save(TicketDTO ticketDTO) {
        log.debug("Request to save TICKET: {}", ticketDTO);
        if (ticketDTO.getId() != null) {
            throw new IllegalArgumentException();
        }

        ticketDTO.setStatus(TicketStatus.CREATED.toString());

        final var ticket = ticketMapper.toEntity(ticketDTO);
        return save(ticket);
    }

    public TicketDTO update(TicketDTO ticketDTO) {
        log.debug("Request to update TICKET: {}", ticketDTO);
        if (ticketDTO.getId() == null) {
            throw new IllegalArgumentException();
        }
        final var ticket = ticketMapper.toEntity(ticketDTO);
        return save(ticket);
    }

    private TicketDTO save(Ticket ticket) {
        BookingDTO bookingDTO = BookingDTO.builder()
                .userId(ticket.getUser().getId())
                .withChild(ticket.getWithChild())
                .localTime(LocalTime.parse(ticket.getTime()))
                .destinationIds(ticket.getDestinationList().stream().map(Destination::getId).collect(Collectors.toList()))
                .build();

        ticket.setPrice(bookingService.calculatePrice(bookingDTO));
        final var savedTicket = ticketRepo.save(ticket);
        return ticketMapper.toDTO(savedTicket);
    }

    public void cancel(Long id) {
        log.debug("Request to delete TICKET with ID: {}", id);
        TicketDTO ticketDTO = this.getOne(id);
        ticketDTO.setStatus(TicketStatus.CANCELED.toString());
        ticketRepo.save(ticketMapper.toEntity(ticketDTO));
    }
}
