package com.example.app.services;

import com.example.app.entities.User;
import com.example.app.repos.UserRepo;
import com.example.app.services.dtos.UserDTO;
import com.example.app.services.mappers.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {
    private final UserRepo userRepo;
    private final UserMapper userMapper;

    public UserService(UserRepo userRepo, UserMapper userMapper) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
    }

    public Page<UserDTO> getAll(Pageable pageable) {
        log.debug("Request to get all USERS");
        return userRepo.findAll(pageable).map(userMapper::toDTO);
    }

    public UserDTO getOne(Long id) {
        log.debug("Request to get USER by ID: {}", id);
        return userMapper.toDTO(userRepo.findById(id).orElse(null));
    }

    public UserDTO save(UserDTO userDTO) {
        log.debug("Request to save USER: {}", userDTO);
        if (userDTO.getId() != null) {
            throw new IllegalArgumentException();
        }
        final var user = userMapper.toEntity(userDTO);
        return save(user);
    }

    public UserDTO update(UserDTO userDTO) {
        log.debug("Request to update USER: {}", userDTO);
        if (userDTO.getId() == null) {
            throw new IllegalArgumentException();
        }
        final var user = userMapper.toEntity(userDTO);
        return save(user);
    }

    private UserDTO save(User user) {
        final var savedUser = userRepo.save(user);
        return userMapper.toDTO(savedUser);
    }

    public void delete(Long id) {
        log.debug("Request to delete USER with ID: {}", id);
        userRepo.deleteById(id);
    }
}
