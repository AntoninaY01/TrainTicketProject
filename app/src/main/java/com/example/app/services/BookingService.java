package com.example.app.services;

import com.example.app.entities.enums.PassengerType;
import com.example.app.services.dtos.BookingDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

@Service
public class BookingService {
    private static final BigDecimal INIT_PRICE_BETWEEN_2_CITIES = BigDecimal.valueOf(10);
    private final UserService userService;
    private final DestinationService destinationService;

    public BookingService(UserService userService, DestinationService destinationService) {
        this.userService = userService;
        this.destinationService = destinationService;
    }

    public BigDecimal calculatePrice(BookingDTO bookingDTO) {
        Long userId = bookingDTO.getUserId();
        var user = userService.getOne(userId);
        if (user == null) return BigDecimal.ZERO;

        List<Long> destinationIds = bookingDTO.getDestinationIds();
        if (destinationIds.size() == 0 || destinationIds.stream().anyMatch(id -> destinationService.getOne(id) == null)) {
         return BigDecimal.ZERO;
        }

        Boolean withChild = bookingDTO.getWithChild();
        LocalTime time = bookingDTO.getLocalTime();

        var price = INIT_PRICE_BETWEEN_2_CITIES.multiply(BigDecimal.valueOf(destinationIds.size() - 1));

        if (!(time.isBefore((LocalTime.of(9, 30))) ||
                (time.isAfter(LocalTime.of(16, 0)) && time.isBefore(LocalTime.of(19, 30))))) {
            price = price.multiply(BigDecimal.valueOf(0.95));
        }

        if (user.getType() != null) {
            if (user.getType().equals(PassengerType.OVER_60.toString())) {
                price = price.multiply(BigDecimal.valueOf(0.66));
            } else if (withChild && user.getType().equals(PassengerType.FAMILY.toString())) {
                price = price.multiply(BigDecimal.valueOf(0.5));
            } else if (withChild) {
                price = price.multiply(BigDecimal.valueOf(0.9));
            }
        }

        return price.setScale(2);
    }
}
