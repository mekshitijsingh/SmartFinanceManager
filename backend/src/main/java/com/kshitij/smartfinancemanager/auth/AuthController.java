package com.kshitij.smartfinancemanager.auth;

import com.kshitij.smartfinancemanager.auth.dto.RegisterRequest;
import com.kshitij.smartfinancemanager.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.kshitij.smartfinancemanager.auth.dto.LoginRequest;
import com.kshitij.smartfinancemanager.auth.dto.AuthResponse;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse<String> register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }
    
    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
    
    @GetMapping("/protected")
    public String protectedEndpoint() {
        return "You accessed a protected route!";
    }

}
