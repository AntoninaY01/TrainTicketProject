package com.example.app.services;

import com.example.app.entities.enums.PassengerType;
import com.example.app.services.dtos.BookingDTO;
import com.example.app.services.dtos.DestinationDTO;
import com.example.app.services.dtos.UserDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
public class BookingServiceT {

    @MockBean
    private UserService userService;

    @MockBean
    private DestinationService destinationService;

    @Autowired
    private BookingService bookingService;

    private AtomicLong destinationId = new AtomicLong(0);

    private UserDTO createUser(PassengerType type, int age) {
        return new UserDTO(
                1L,
                "aaaa",
                "bbbb",
                age,
                type.toString()
        );
    }

    private DestinationDTO createDestination(String name) {
        return new DestinationDTO(destinationId.incrementAndGet(), name);
    }

    private BookingDTO createBooking(UserDTO userDTO, Boolean withChild, LocalTime departureTime, Long... destinationIds){
        return new BookingDTO(userDTO.getId(), withChild, departureTime, List.of(destinationIds));
    }

    @Test
    void calculatePrice_getPriceForNormalUserNoChildInRushHour() {
        UserDTO userDTO = createUser(PassengerType.NONE, 22);
        Mockito.when(userService.getOne(anyLong())).thenReturn(userDTO);

        DestinationDTO firstDestination = createDestination("Sofia");
        Mockito.when(destinationService.getOne(firstDestination.getId())).thenReturn(firstDestination);
        DestinationDTO secondDestination = createDestination("Plovdiv");
        Mockito.when(destinationService.getOne(secondDestination.getId())).thenReturn(secondDestination);

        BookingDTO bookingDTO = createBooking(
                userDTO,
                Boolean.FALSE,
                LocalTime.of(8, 30),
                firstDestination.getId(), secondDestination.getId()
        );

        BigDecimal price = bookingService.calculatePrice(bookingDTO);

        Assertions.assertEquals(BigDecimal.valueOf(10).setScale(2), price);
    }

    @Test
    void calculatePrice_getPriceForNormalUserNoChildInNormalHour() {
        UserDTO userDTO = createUser(PassengerType.NONE, 22);
        Mockito.when(userService.getOne(anyLong())).thenReturn(userDTO);

        DestinationDTO firstDestination = createDestination("Sofia");
        Mockito.when(destinationService.getOne(firstDestination.getId())).thenReturn(firstDestination);
        DestinationDTO secondDestination = createDestination("Plovdiv");
        Mockito.when(destinationService.getOne(secondDestination.getId())).thenReturn(secondDestination);

        BookingDTO bookingDTO = createBooking(
                userDTO,
                Boolean.FALSE,
                LocalTime.of(12, 30),
                firstDestination.getId(), secondDestination.getId()
        );

        BigDecimal price = bookingService.calculatePrice(bookingDTO);

        Assertions.assertEquals(BigDecimal.valueOf(9.5).setScale(2), price);
    }

    @Test
    void calculatePrice_getPriceForNormalUserWithChildInRushHour(){
        UserDTO userDTO = createUser(PassengerType.NONE, 22);
        Mockito.when(userService.getOne(anyLong())).thenReturn(userDTO);

        DestinationDTO firstDestination = createDestination("Sofia");
        Mockito.when(destinationService.getOne(firstDestination.getId())).thenReturn(firstDestination);
        DestinationDTO secondDestination = createDestination("Plovdiv");
        Mockito.when(destinationService.getOne(secondDestination.getId())).thenReturn(secondDestination);

        BookingDTO bookingDTO = createBooking(
                userDTO,
                Boolean.TRUE,
                LocalTime.of(17, 30),
                firstDestination.getId(), secondDestination.getId()
        );

        BigDecimal price = bookingService.calculatePrice(bookingDTO);

        Assertions.assertEquals(BigDecimal.valueOf(9).setScale(2), price);
    }

    @Test
    void calculatePrice_getPriceForNormalUserWithChildInNormalHour(){
        UserDTO userDTO = createUser(PassengerType.NONE, 22);
        Mockito.when(userService.getOne(anyLong())).thenReturn(userDTO);

        DestinationDTO firstDestination = createDestination("Sofia");
        Mockito.when(destinationService.getOne(firstDestination.getId())).thenReturn(firstDestination);
        DestinationDTO secondDestination = createDestination("Plovdiv");
        Mockito.when(destinationService.getOne(secondDestination.getId())).thenReturn(secondDestination);

        BookingDTO bookingDTO = createBooking(
                userDTO,
                Boolean.TRUE,
                LocalTime.of(11, 30),
                firstDestination.getId(), secondDestination.getId()
        );

        BigDecimal price = bookingService.calculatePrice(bookingDTO);

        Assertions.assertEquals(BigDecimal.valueOf(8.55), price);
    }

    @Test
    void calculatePrice_oneDestinationResultsInZero(){
        UserDTO userDTO = createUser(PassengerType.NONE, 22);
        Mockito.when(userService.getOne(anyLong())).thenReturn(userDTO);

        DestinationDTO firstDestination = createDestination("Sofia");
        Mockito.when(destinationService.getOne(firstDestination.getId())).thenReturn(firstDestination);

        BookingDTO bookingDTO = createBooking(
                userDTO,
                Boolean.FALSE,
                LocalTime.of(12, 30),
                firstDestination.getId()
        );

        BigDecimal price = bookingService.calculatePrice(bookingDTO);

        Assertions.assertEquals(BigDecimal.valueOf(0).setScale(2), price);
    }

    @Test
    void calculatePrice_getPriceForOver60NoChildInRushHour(){
        UserDTO userDTO = createUser(PassengerType.OVER_60, 65);
        Mockito.when(userService.getOne(anyLong())).thenReturn(userDTO);

        DestinationDTO firstDestination = createDestination("Sofia");
        Mockito.when(destinationService.getOne(firstDestination.getId())).thenReturn(firstDestination);
        DestinationDTO secondDestination = createDestination("Plovdiv");
        Mockito.when(destinationService.getOne(secondDestination.getId())).thenReturn(secondDestination);

        BookingDTO bookingDTO = createBooking(
                userDTO,
                Boolean.FALSE,
                LocalTime.of(9, 00),
                firstDestination.getId(), secondDestination.getId()
        );

        BigDecimal price = bookingService.calculatePrice(bookingDTO);

        Assertions.assertEquals(BigDecimal.valueOf(6.6).setScale(2), price);
    }

    @Test
    void calculatePrice_getPriceForOver60NoChildInNormalHour(){
        UserDTO userDTO = createUser(PassengerType.OVER_60, 65);
        Mockito.when(userService.getOne(anyLong())).thenReturn(userDTO);

        DestinationDTO firstDestination = createDestination("Sofia");
        Mockito.when(destinationService.getOne(firstDestination.getId())).thenReturn(firstDestination);
        DestinationDTO secondDestination = createDestination("Plovdiv");
        Mockito.when(destinationService.getOne(secondDestination.getId())).thenReturn(secondDestination);

        BookingDTO bookingDTO = createBooking(
                userDTO,
                Boolean.FALSE,
                LocalTime.of(12, 00),
                firstDestination.getId(), secondDestination.getId()
        );

        BigDecimal price = bookingService.calculatePrice(bookingDTO);

        Assertions.assertEquals(BigDecimal.valueOf(6.27).setScale(2), price);
    }

    @Test
    void calculatePrice_priceForOver60DoesNotChangeWithChildInNormalHour(){
        UserDTO userDTO = createUser(PassengerType.OVER_60, 65);
        Mockito.when(userService.getOne(anyLong())).thenReturn(userDTO);

        DestinationDTO firstDestination = createDestination("Sofia");
        Mockito.when(destinationService.getOne(firstDestination.getId())).thenReturn(firstDestination);
        DestinationDTO secondDestination = createDestination("Plovdiv");
        Mockito.when(destinationService.getOne(secondDestination.getId())).thenReturn(secondDestination);

        BookingDTO bookingDTO = createBooking(
                userDTO,
                Boolean.TRUE,
                LocalTime.of(12, 00),
                firstDestination.getId(), secondDestination.getId()
        );

        BigDecimal price = bookingService.calculatePrice(bookingDTO);

        Assertions.assertEquals(BigDecimal.valueOf(6.27).setScale(2), price);
    }

    @Test
    void calculatePrice_getPriceForFamilyTypeWithChildInRushHour(){
        UserDTO userDTO = createUser(PassengerType.FAMILY, 65);
        Mockito.when(userService.getOne(anyLong())).thenReturn(userDTO);

        DestinationDTO firstDestination = createDestination("Sofia");
        Mockito.when(destinationService.getOne(firstDestination.getId())).thenReturn(firstDestination);
        DestinationDTO secondDestination = createDestination("Plovdiv");
        Mockito.when(destinationService.getOne(secondDestination.getId())).thenReturn(secondDestination);

        BookingDTO bookingDTO = createBooking(
                userDTO,
                Boolean.TRUE,
                LocalTime.of(17, 00),
                firstDestination.getId(), secondDestination.getId()
        );

        BigDecimal price = bookingService.calculatePrice(bookingDTO);

        Assertions.assertEquals(BigDecimal.valueOf(5).setScale(2), price);
    }

    @Test
    void calculatePrice_getPriceForFamilyNoChildInRushHour(){
        UserDTO userDTO = createUser(PassengerType.FAMILY, 65);
        Mockito.when(userService.getOne(anyLong())).thenReturn(userDTO);

        DestinationDTO firstDestination = createDestination("Sofia");
        Mockito.when(destinationService.getOne(firstDestination.getId())).thenReturn(firstDestination);
        DestinationDTO secondDestination = createDestination("Plovdiv");
        Mockito.when(destinationService.getOne(secondDestination.getId())).thenReturn(secondDestination);

        BookingDTO bookingDTO = createBooking(
                userDTO,
                Boolean.FALSE,
                LocalTime.of(17, 00),
                firstDestination.getId(), secondDestination.getId()
        );

        BigDecimal price = bookingService.calculatePrice(bookingDTO);

        Assertions.assertEquals(BigDecimal.valueOf(10).setScale(2), price);
    }

    @Test
    void calculatePrice_getPriceForFamilyWithChildInNormalHour(){
        UserDTO userDTO = createUser(PassengerType.FAMILY, 65);
        Mockito.when(userService.getOne(anyLong())).thenReturn(userDTO);

        DestinationDTO firstDestination = createDestination("Sofia");
        Mockito.when(destinationService.getOne(firstDestination.getId())).thenReturn(firstDestination);
        DestinationDTO secondDestination = createDestination("Plovdiv");
        Mockito.when(destinationService.getOne(secondDestination.getId())).thenReturn(secondDestination);

        BookingDTO bookingDTO = createBooking(
                userDTO,
                Boolean.TRUE,
                LocalTime.of(14, 00),
                firstDestination.getId(), secondDestination.getId()
        );

        BigDecimal price = bookingService.calculatePrice(bookingDTO);

        Assertions.assertEquals(BigDecimal.valueOf(4.75).setScale(2), price);
    }

    @Test
    void calculatePrice_getPriceForMultiwayWithOneStopForNormalUserNoChildInRushHour(){
        UserDTO userDTO = createUser(PassengerType.NONE, 22);
        Mockito.when(userService.getOne(anyLong())).thenReturn(userDTO);

        DestinationDTO firstDestination = createDestination("Sofia");
        Mockito.when(destinationService.getOne(firstDestination.getId())).thenReturn(firstDestination);
        DestinationDTO secondDestination = createDestination("Pazardzhik");
        Mockito.when(destinationService.getOne(secondDestination.getId())).thenReturn(secondDestination);
        DestinationDTO finalDestination = createDestination("Plovdiv");
        Mockito.when(destinationService.getOne(finalDestination.getId())).thenReturn(finalDestination);

        BookingDTO bookingDTO = createBooking(
                userDTO,
                Boolean.FALSE,
                LocalTime.of(18, 00),
                firstDestination.getId(), secondDestination.getId(), finalDestination.getId()
        );

        BigDecimal price = bookingService.calculatePrice(bookingDTO);

        Assertions.assertEquals(BigDecimal.valueOf(20).setScale(2), price);
    }

    @Test
    void calculatePrice_getPriceForMultiwayWithTwoStopsForNormalUserNoChildInRushHour(){
        UserDTO userDTO = createUser(PassengerType.NONE, 22);
        Mockito.when(userService.getOne(anyLong())).thenReturn(userDTO);

        DestinationDTO firstDestination = createDestination("Sofia");
        Mockito.when(destinationService.getOne(firstDestination.getId())).thenReturn(firstDestination);
        DestinationDTO secondDestination = createDestination("Pazardzhik");
        Mockito.when(destinationService.getOne(secondDestination.getId())).thenReturn(secondDestination);
        DestinationDTO thirdDestination = createDestination("Plovdiv");
        Mockito.when(destinationService.getOne(thirdDestination.getId())).thenReturn(thirdDestination);
        DestinationDTO finalDestination = createDestination("Stara Zagora");
        Mockito.when(destinationService.getOne(finalDestination.getId())).thenReturn(finalDestination);

        BookingDTO bookingDTO = createBooking(
                userDTO,
                Boolean.FALSE,
                LocalTime.of(18, 00),
                firstDestination.getId(), secondDestination.getId(), thirdDestination.getId(), finalDestination.getId()
        );

        BigDecimal price = bookingService.calculatePrice(bookingDTO);

        Assertions.assertEquals(BigDecimal.valueOf(30).setScale(2), price);
    }
}
