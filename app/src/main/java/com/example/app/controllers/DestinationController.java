package com.example.app.controllers;

import com.example.app.services.DestinationService;
import com.example.app.services.dtos.DestinationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/destinations")
public class DestinationController {
    private final DestinationService destinationService;

    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    @GetMapping
    public ResponseEntity<Page<DestinationDTO>> getAll(Pageable pageable) {
        log.debug("REST request to get all DESTINATIONS sorted by {}, page number: {} and page size: {}",
                pageable.getSort(), pageable.getPageNumber(), pageable.getPageSize());
        final var destinations = destinationService.getAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(destinations);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DestinationDTO> getOne(@PathVariable Long id) {
        log.debug("REST request to get DESTINATION by ID: {}", id);
        final var destination = destinationService.getOne(id);
        return ResponseEntity.status(HttpStatus.OK).body(destination);
    }
    
    @PostMapping
    public ResponseEntity<DestinationDTO> save(@RequestBody DestinationDTO destinationDTO) {
        log.debug("REST request to save DESTINATION with content: {}", destinationDTO);
        final var savedDestination = destinationService.save(destinationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDestination);
    }
    
    @PutMapping
    public ResponseEntity<DestinationDTO> update(@RequestBody DestinationDTO destinationDTO) {
        log.debug("REST request to updated DESTINATION with ID: {} with content {}", destinationDTO.getId(), destinationDTO);
        final var updateDestination = destinationService.update(destinationDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updateDestination);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete DESTINATION with ID: {}", id);
        destinationService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
