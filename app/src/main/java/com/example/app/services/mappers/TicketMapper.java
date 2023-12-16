package com.example.app.services.mappers;

import com.example.app.entities.Ticket;
import com.example.app.services.dtos.TicketDTO;
import org.mapstruct.*;

import java.time.LocalTime;

@Mapper(componentModel = "spring", uses = {UserMapper.class, DestinationMapper.class})
public interface TicketMapper {

    @Mapping(target = "time", source = "parsedTime", qualifiedByName = "convertLocalTimeToString")
    Ticket toEntity(TicketDTO ticketDTO);

    @Mapping(target = "parsedTime", source = "time", qualifiedByName = "convertStringToLocalTime")
    TicketDTO toDTO(Ticket ticket);

    @Named("convertStringToLocalTime")
    static LocalTime convertStringToLocalTime(String time) {
        return time != null ? LocalTime.parse(time) : null;
    }

    @Named("convertLocalTimeToString")
    static String convertLocalTimeToString(LocalTime parsedTime) {
        return parsedTime != null ? parsedTime.toString() : null;
    }


}
