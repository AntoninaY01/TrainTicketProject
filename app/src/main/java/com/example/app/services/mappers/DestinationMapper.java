package com.example.app.services.mappers;

import com.example.app.entities.Destination;
import com.example.app.services.dtos.DestinationDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DestinationMapper {
    Destination toEntity(DestinationDTO destinationDTO);
    DestinationDTO toDTO(Destination destination);
}
