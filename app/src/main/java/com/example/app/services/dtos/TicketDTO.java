package com.example.app.services.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketDTO {
    private Long id;
    private UserDTO user;
    private LocalTime parsedTime;
    private Boolean withChild;
    private String status;
    private BigDecimal price;
    private List<DestinationDTO> destinationList = new ArrayList<>();
}
