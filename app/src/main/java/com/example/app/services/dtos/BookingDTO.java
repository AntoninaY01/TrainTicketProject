package com.example.app.services.dtos;

import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private Long userId;
    private Boolean withChild;
    private LocalTime localTime;
    private List<Long> destinationIds;
}
