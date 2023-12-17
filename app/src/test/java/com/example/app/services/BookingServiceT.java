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
                "test",
                "test1234",
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
    void getStandardPriceNonChildDuringPeakHours() {
        UserDTO userDTO = createUser(PassengerType.NONE, 22);
        Mockito.when(userService.getOne(anyLong())).thenReturn(userDTO);

        DestinationDTO firstDestination = createDestination("Sofia");
        Mockito.when(destinationService.getOne(firstDestination.getId())).thenReturn(firstDestination);
        DestinationDTO secondDestination = createDestination("Plovdiv");
        Mockito.when(destinationService.getOne(secondDestination.getId())).thenReturn(secondDestination);

        BookingDTO bookingDTO = createBooking(
                userDTO,
                Boolean.FALSE,
                LocalTime.of(7, 00),
                firstDestination.getId(), secondDestination.getId()
        );

        BigDecimal price = bookingService.calculatePrice(bookingDTO);

        Assertions.assertEquals(BigDecimal.valueOf(20).setScale(2), price);
    }

    @Test
    void getStandardPriceNonChildDuringRegularHours() {
        UserDTO userDTO = createUser(PassengerType.NONE, 22);
        Mockito.when(userService.getOne(anyLong())).thenReturn(userDTO);

        DestinationDTO firstDestination = createDestination("Chirpan");
        Mockito.when(destinationService.getOne(firstDestination.getId())).thenReturn(firstDestination);
        DestinationDTO secondDestination = createDestination("Pernik");
        Mockito.when(destinationService.getOne(secondDestination.getId())).thenReturn(secondDestination);

        BookingDTO bookingDTO = createBooking(
                userDTO,
                Boolean.FALSE,
                LocalTime.of(13, 45),
                firstDestination.getId(), secondDestination.getId()
        );

        BigDecimal price = bookingService.calculatePrice(bookingDTO);

        Assertions.assertEquals(BigDecimal.valueOf(19).setScale(2), price);
    }

    @Test
    void getStandardPriceWithChildDuringPeakHours(){
        UserDTO userDTO = createUser(PassengerType.NONE, 22);
        Mockito.when(userService.getOne(anyLong())).thenReturn(userDTO);

        DestinationDTO firstDestination = createDestination("Sofia");
        Mockito.when(destinationService.getOne(firstDestination.getId())).thenReturn(firstDestination);
        DestinationDTO secondDestination = createDestination("Burgas");
        Mockito.when(destinationService.getOne(secondDestination.getId())).thenReturn(secondDestination);

        BookingDTO bookingDTO = createBooking(
                userDTO,
                Boolean.TRUE,
                LocalTime.of(18, 05),
                firstDestination.getId(), secondDestination.getId()
        );

        BigDecimal price = bookingService.calculatePrice(bookingDTO);

        Assertions.assertEquals(BigDecimal.valueOf(18).setScale(2), price);
    }

    @Test
    void getStandardPriceWithChildDuringRegularHours(){
        UserDTO userDTO = createUser(PassengerType.NONE, 22);
        Mockito.when(userService.getOne(anyLong())).thenReturn(userDTO);

        DestinationDTO firstDestination = createDestination("Pernik");
        Mockito.when(destinationService.getOne(firstDestination.getId())).thenReturn(firstDestination);
        DestinationDTO secondDestination = createDestination("Petrich");
        Mockito.when(destinationService.getOne(secondDestination.getId())).thenReturn(secondDestination);

        BookingDTO bookingDTO = createBooking(
                userDTO,
                Boolean.TRUE,
                LocalTime.of(11, 30),
                firstDestination.getId(), secondDestination.getId()
        );

        BigDecimal price = bookingService.calculatePrice(bookingDTO);

        Assertions.assertEquals(BigDecimal.valueOf(17.1).setScale(2), price);
    }

    @Test
    void singleDestinationResultsInZero(){
        UserDTO userDTO = createUser(PassengerType.NONE, 22);
        Mockito.when(userService.getOne(anyLong())).thenReturn(userDTO);

        DestinationDTO firstDestination = createDestination("Sofia");
        Mockito.when(destinationService.getOne(firstDestination.getId())).thenReturn(firstDestination);

        BookingDTO bookingDTO = createBooking(
                userDTO,
                Boolean.FALSE,
                LocalTime.of(13, 15),
                firstDestination.getId()
        );

        BigDecimal price = bookingService.calculatePrice(bookingDTO);

        Assertions.assertEquals(BigDecimal.valueOf(0).setScale(2), price);
    }

    @Test
    void getPriceForSeniorNoChildDuringPeakHours(){
        UserDTO userDTO = createUser(PassengerType.OVER_60, 65);
        Mockito.when(userService.getOne(anyLong())).thenReturn(userDTO);

        DestinationDTO firstDestination = createDestination("Balchik");
        Mockito.when(destinationService.getOne(firstDestination.getId())).thenReturn(firstDestination);
        DestinationDTO secondDestination = createDestination("Samokov");
        Mockito.when(destinationService.getOne(secondDestination.getId())).thenReturn(secondDestination);

        BookingDTO bookingDTO = createBooking(
                userDTO,
                Boolean.FALSE,
                LocalTime.of(9, 00),
                firstDestination.getId(), secondDestination.getId()
        );

        BigDecimal price = bookingService.calculatePrice(bookingDTO);

        Assertions.assertEquals(BigDecimal.valueOf(13.2).setScale(2), price);
    }

    @Test
    void getPriceForSeniorNoChildDuringRegularHours(){
        UserDTO userDTO = createUser(PassengerType.OVER_60, 65);
        Mockito.when(userService.getOne(anyLong())).thenReturn(userDTO);

        DestinationDTO firstDestination = createDestination("Sofia");
        Mockito.when(destinationService.getOne(firstDestination.getId())).thenReturn(firstDestination);
        DestinationDTO secondDestination = createDestination("Varna");
        Mockito.when(destinationService.getOne(secondDestination.getId())).thenReturn(secondDestination);

        BookingDTO bookingDTO = createBooking(
                userDTO,
                Boolean.FALSE,
                LocalTime.of(16, 00),
                firstDestination.getId(), secondDestination.getId()
        );

        BigDecimal price = bookingService.calculatePrice(bookingDTO);

        Assertions.assertEquals(BigDecimal.valueOf(12.54).setScale(2), price);
    }

    @Test
    void priceForSeniorRemainsUnchangedWithChildDuringRegularHours(){
        UserDTO userDTO = createUser(PassengerType.OVER_60, 65);
        Mockito.when(userService.getOne(anyLong())).thenReturn(userDTO);

        DestinationDTO firstDestination = createDestination("Plovdiv");
        Mockito.when(destinationService.getOne(firstDestination.getId())).thenReturn(firstDestination);
        DestinationDTO secondDestination = createDestination("Stara Zagora");
        Mockito.when(destinationService.getOne(secondDestination.getId())).thenReturn(secondDestination);

        BookingDTO bookingDTO = createBooking(
                userDTO,
                Boolean.TRUE,
                LocalTime.of(13, 45),
                firstDestination.getId(), secondDestination.getId()
        );

        BigDecimal price = bookingService.calculatePrice(bookingDTO);

        Assertions.assertEquals(BigDecimal.valueOf(12.54).setScale(2), price);
    }

    @Test
    void getPriceForFamilyWithChildDuringPeakHours(){
        UserDTO userDTO = createUser(PassengerType.FAMILY, 65);
        Mockito.when(userService.getOne(anyLong())).thenReturn(userDTO);

        DestinationDTO firstDestination = createDestination("Plovdiv");
        Mockito.when(destinationService.getOne(firstDestination.getId())).thenReturn(firstDestination);
        DestinationDTO secondDestination = createDestination("Radomir");
        Mockito.when(destinationService.getOne(secondDestination.getId())).thenReturn(secondDestination);

        BookingDTO bookingDTO = createBooking(
                userDTO,
                Boolean.TRUE,
                LocalTime.of(18, 00),
                firstDestination.getId(), secondDestination.getId()
        );

        BigDecimal price = bookingService.calculatePrice(bookingDTO);

        Assertions.assertEquals(BigDecimal.valueOf(10).setScale(2), price);
    }

    @Test
    void getPriceForFamilyNoChildDuringPeakHours(){
        UserDTO userDTO = createUser(PassengerType.FAMILY, 65);
        Mockito.when(userService.getOne(anyLong())).thenReturn(userDTO);

        DestinationDTO firstDestination = createDestination("Pernik");
        Mockito.when(destinationService.getOne(firstDestination.getId())).thenReturn(firstDestination);
        DestinationDTO secondDestination = createDestination("Sofia");
        Mockito.when(destinationService.getOne(secondDestination.getId())).thenReturn(secondDestination);

        BookingDTO bookingDTO = createBooking(
                userDTO,
                Boolean.FALSE,
                LocalTime.of(19, 15),
                firstDestination.getId(), secondDestination.getId()
        );

        BigDecimal price = bookingService.calculatePrice(bookingDTO);

        Assertions.assertEquals(BigDecimal.valueOf(20).setScale(2), price);
    }

    @Test
    void getPriceForSeniorWithChildDuringPeakHours(){
        UserDTO userDTO = createUser(PassengerType.OVER_60, 70);
        Mockito.when(userService.getOne(anyLong())).thenReturn(userDTO);

        DestinationDTO firstDestination = createDestination("Pernik");
        Mockito.when(destinationService.getOne(firstDestination.getId())).thenReturn(firstDestination);
        DestinationDTO secondDestination = createDestination("Sofia");
        Mockito.when(destinationService.getOne(secondDestination.getId())).thenReturn(secondDestination);

        BookingDTO bookingDTO = createBooking(
                userDTO,
                Boolean.TRUE,
                LocalTime.of(19, 15),
                firstDestination.getId(), secondDestination.getId()
        );

        BigDecimal price = bookingService.calculatePrice(bookingDTO);

        Assertions.assertEquals(BigDecimal.valueOf(13.2).setScale(2), price);
    }

    @Test
    void getPriceForFamilyWithChildDuringRegularHours(){
        UserDTO userDTO = createUser(PassengerType.FAMILY, 65);
        Mockito.when(userService.getOne(anyLong())).thenReturn(userDTO);

        DestinationDTO firstDestination = createDestination("Dupnica");
        Mockito.when(destinationService.getOne(firstDestination.getId())).thenReturn(firstDestination);
        DestinationDTO secondDestination = createDestination("Plovdiv");
        Mockito.when(destinationService.getOne(secondDestination.getId())).thenReturn(secondDestination);

        BookingDTO bookingDTO = createBooking(
                userDTO,
                Boolean.TRUE,
                LocalTime.of(15, 15),
                firstDestination.getId(), secondDestination.getId()
        );

        BigDecimal price = bookingService.calculatePrice(bookingDTO);

        Assertions.assertEquals(BigDecimal.valueOf(9.5).setScale(2), price);
    }

    @Test
    void getPriceForMultiwayWithOneStopNormalUserNoChildDuringPeakHours(){
        UserDTO userDTO = createUser(PassengerType.NONE, 22);
        Mockito.when(userService.getOne(anyLong())).thenReturn(userDTO);

        DestinationDTO firstDestination = createDestination("Sofia");
        Mockito.when(destinationService.getOne(firstDestination.getId())).thenReturn(firstDestination);
        DestinationDTO secondDestination = createDestination("Burgas");
        Mockito.when(destinationService.getOne(secondDestination.getId())).thenReturn(secondDestination);
        DestinationDTO finalDestination = createDestination("Varna");
        Mockito.when(destinationService.getOne(finalDestination.getId())).thenReturn(finalDestination);

        BookingDTO bookingDTO = createBooking(
                userDTO,
                Boolean.FALSE,
                LocalTime.of(22, 00),
                firstDestination.getId(), secondDestination.getId(), finalDestination.getId()
        );

        BigDecimal price = bookingService.calculatePrice(bookingDTO);

        Assertions.assertEquals(BigDecimal.valueOf(38).setScale(2), price);
    }

    @Test
    void getPriceForMultiwayWithTwoStopsNormalUserNoChildDuringPeakHours(){
        UserDTO userDTO = createUser(PassengerType.NONE, 22);
        Mockito.when(userService.getOne(anyLong())).thenReturn(userDTO);

        DestinationDTO firstDestination = createDestination("Sofia");
        Mockito.when(destinationService.getOne(firstDestination.getId())).thenReturn(firstDestination);
        DestinationDTO secondDestination = createDestination("Burgas");
        Mockito.when(destinationService.getOne(secondDestination.getId())).thenReturn(secondDestination);
        DestinationDTO thirdDestination = createDestination("Plovdiv");
        Mockito.when(destinationService.getOne(thirdDestination.getId())).thenReturn(thirdDestination);
        DestinationDTO finalDestination = createDestination("Pernik");
        Mockito.when(destinationService.getOne(finalDestination.getId())).thenReturn(finalDestination);

        BookingDTO bookingDTO = createBooking(
                userDTO,
                Boolean.FALSE,
                LocalTime.of(22, 00),
                firstDestination.getId(), secondDestination.getId(), thirdDestination.getId(), finalDestination.getId()
        );

        BigDecimal price = bookingService.calculatePrice(bookingDTO);

        Assertions.assertEquals(BigDecimal.valueOf(57).setScale(2), price);
    }
}
