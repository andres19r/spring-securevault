package com.andozy.security_api.service;

import com.andozy.security_api.auth.JwtService;
import com.andozy.security_api.auth.SecurityUser;
import com.andozy.security_api.dto.*;
import com.andozy.security_api.entity.Role;
import com.andozy.security_api.entity.User;
import com.andozy.security_api.exception.EmailAlreadyExistsException;
import com.andozy.security_api.repository.UserRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public MessageResponse register(RegisterRequest request) {
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
        return new MessageResponse("User registered successfully");
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        var authRequest = new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
        );
        var authentication = authenticationManager.authenticate(authRequest);
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        User user = securityUser.getUser();
        String token = jwtService.generateAccessToken(user);
        GeneratedToken refreshToken = refreshTokenService.createForLogin(user);
        return new AuthResponse("Bearer", token, refreshToken.rawToken());
    }
}
