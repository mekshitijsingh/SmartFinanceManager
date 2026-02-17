package com.kshitij.smartfinancemanager.auth;

import com.kshitij.smartfinancemanager.auth.dto.RegisterRequest;
import com.kshitij.smartfinancemanager.common.ApiResponse;
import com.kshitij.smartfinancemanager.exception.BadRequestException;
import com.kshitij.smartfinancemanager.exception.UnauthorizedException;
import com.kshitij.smartfinancemanager.user.Role;
import com.kshitij.smartfinancemanager.user.User;
import com.kshitij.smartfinancemanager.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.kshitij.smartfinancemanager.auth.dto.LoginRequest;
import com.kshitij.smartfinancemanager.auth.dto.AuthResponse;
import com.kshitij.smartfinancemanager.security.JwtService;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    private final JwtService jwtService;

    public ApiResponse<String> register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
        	throw new BadRequestException("Email is already registered");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        return ApiResponse.success("User registered successfully", null);
    }
    
    public ApiResponse<AuthResponse> login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
        	throw new UnauthorizedException("Invalid email or password");

        }

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        AuthResponse response = AuthResponse.builder()
                .token(token)
                .build();

        return ApiResponse.success("Login successful", response);
    }

}
