package com.andozy.security_api.data;

import com.andozy.security_api.entity.Role;
import com.andozy.security_api.entity.User;
import com.andozy.security_api.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
public class DataSeeder {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner seedUser() {
        return args -> {
            if (userRepository.findByEmail("test@test.com").isEmpty()) {
                userRepository.save(User.builder()
                        .email("test@test.com")
                        .password(passwordEncoder.encode("password123"))
                        .role(Role.USER)
                        .build());
            }
        };
    }
}
