package com.example.app.services;

import com.example.app.entities.Destination;
import com.example.app.repos.DestinationRepo;
import com.example.app.services.dtos.DestinationDTO;
import com.example.app.services.mappers.DestinationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DestinationService {
    private final DestinationRepo destinationRepo;
    private final DestinationMapper destinationMapper;

    public DestinationService(DestinationRepo destinationRepo, DestinationMapper destinationMapper) {
        this.destinationRepo = destinationRepo;
        this.destinationMapper = destinationMapper;
    }

    public Page<DestinationDTO> getAll(Pageable pageable) {
        return destinationRepo.findAll(pageable).map(destinationMapper::toDTO);
    }

    public DestinationDTO getOne(Long id) {
        return destinationMapper.toDTO(destinationRepo.findById(id).orElse(null));
    }

    public DestinationDTO save(DestinationDTO destinationDTO) {
        if (destinationDTO.getId() != null) {
            throw new IllegalArgumentException();
        }
        final var destination = destinationMapper.toEntity(destinationDTO);
        return save(destination);
    }

    public DestinationDTO update(DestinationDTO destinationDTO) {
        if (destinationDTO.getId() == null) {
            throw new IllegalArgumentException();
        }
        final var destination = destinationMapper.toEntity(destinationDTO);
        return save(destination);
    }

    private DestinationDTO save(Destination destination) {
        final var savedDestination = destinationRepo.save(destination);
        return destinationMapper.toDTO(savedDestination);
    }

    public void delete(Long id) {
        destinationRepo.deleteById(id);
    }
}
