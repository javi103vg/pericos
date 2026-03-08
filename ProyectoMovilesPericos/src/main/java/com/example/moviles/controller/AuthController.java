package com.example.moviles.controller;

import com.example.moviles.model.dto.LoginRequestDTO;
import com.example.moviles.model.dto.LoginResponseDTO;
import com.example.moviles.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/v1/auth/login
     *
     * Body:
     * {
     *   "email": "admin@moviles.com",
     *   "password": "1234"
     * }
     *
     * Response:
     * {
     *   "token": "eyJhbGci...",
     *   "email": "admin@moviles.com",
     *   "rol": "ADMIN"
     * }
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
