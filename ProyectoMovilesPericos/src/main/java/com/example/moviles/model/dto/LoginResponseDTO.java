package com.example.moviles.model.dto;

public record LoginResponseDTO(
        String token,
        String email,
        String rol
) {}