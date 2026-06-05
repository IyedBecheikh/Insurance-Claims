package com.iyed.insuranceclaims.user.service;

import com.iyed.insuranceclaims.common.exception.ResourceNotFoundException;
import com.iyed.insuranceclaims.user.dto.CreateUserRequestDto;
import com.iyed.insuranceclaims.user.dto.UserResponseDto;
import com.iyed.insuranceclaims.user.entity.User;
import com.iyed.insuranceclaims.user.mapper.UserMapper;
import com.iyed.insuranceclaims.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDto create(CreateUserRequestDto request) {
        userRepository.findByEmail(request.email()).ifPresent(existing -> {
            throw new IllegalStateException("User email already exists");
        });

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(request.role());
        user.setEnabled(request.enabled());
        user.setCreatedAt(LocalDateTime.now());
        return userMapper.toResponseDto(userRepository.save(user));
    }

    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream().map(userMapper::toResponseDto).toList();
    }

    public UserResponseDto findById(UUID id) {
        return userMapper.toResponseDto(getUser(id));
    }

    public UserResponseDto updateEnabled(UUID id, boolean enabled) {
        User user = getUser(id);
        user.setEnabled(enabled);
        return userMapper.toResponseDto(userRepository.save(user));
    }

    public User getUser(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
