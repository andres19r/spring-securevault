package com.andozy.security_api.service;

import com.andozy.security_api.dto.AuthResponse;
import com.andozy.security_api.dto.RegisterRequest;
import com.andozy.security_api.entity.Role;
import com.andozy.security_api.entity.User;
import com.andozy.security_api.exception.EmailAlreadyExistsException;
import com.andozy.security_api.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse register(RegisterRequest request) {
        Optional<User> user = userRepository.findByEmail(request.email());
        if (user.isPresent()) {
            throw new EmailAlreadyExistsException("Email already registered");
        }
        User newUser = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();
        userRepository.save(newUser);
        return new AuthResponse("User registered successfully");
    }
}
