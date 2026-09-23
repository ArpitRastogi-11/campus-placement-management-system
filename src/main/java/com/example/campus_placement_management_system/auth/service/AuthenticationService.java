package com.example.campus_placement_management_system.auth.service;

import com.example.campus_placement_management_system.auth.dto.LoginRequest;
import com.example.campus_placement_management_system.auth.dto.LoginResponse;
import com.example.campus_placement_management_system.auth.dto.RegisterRequest;
import com.example.campus_placement_management_system.auth.entity.User;
import com.example.campus_placement_management_system.auth.repository.UserRepository;
import com.example.campus_placement_management_system.auth.security.CustomUserDetails;
import com.example.campus_placement_management_system.auth.security.JwtService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already registered");
        }

        if (request.role().name().equals("ADMIN")) {
            throw new RuntimeException("Admin registration is not allowed");
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role())
                .enabled(true)
                .build();

        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.email(),
                                request.password()
                        )
                );

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(userDetails);

        User user = userDetails.getUser();

        return new LoginResponse(
                token,
                "Bearer",
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}
