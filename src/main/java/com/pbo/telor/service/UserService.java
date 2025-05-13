package com.pbo.telor.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pbo.telor.dto.response.UserResponse;
import com.pbo.telor.exception.NotFoundException;
import com.pbo.telor.mapper.UserMapper;
import com.pbo.telor.model.UserEntity;
import com.pbo.telor.repository.UserRepository;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserRepository userRepository;

    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    public Page<UserResponse> findAllPaged(int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size);
        Page<UserEntity> result = userRepository.findAll(pageable);
        return result.map(UserMapper::toResponse);
    }

    public UserResponse findById(UUID id) {
        return userRepository.findById(id)
                .map(UserMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public UserResponse create(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return UserMapper.toResponse(userRepository.save(user));
    }

    public UserResponse update(UUID id, UserEntity userData) {
        return userRepository.findById(id).map(user -> {
            user.setFullname(userData.getFullname());
            user.setEmail(userData.getEmail());
            user.setRole(userData.getRole());
            return UserMapper.toResponse(userRepository.save(user));
        }).orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    public UserResponse patchUser(UUID id, UserEntity patchData) {
        UserEntity user = userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("User not found"));

        if (patchData.getFullname() != null)
            user.setFullname(patchData.getFullname());

        if (patchData.getEmail() != null)
            user.setEmail(patchData.getEmail());

        if (patchData.getRole() != null)
            user.setRole(patchData.getRole());

        return UserMapper.toResponse(userRepository.save(user));
    }

    public void delete(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("User not found");
        }
        userRepository.deleteById(id);
    }
}

