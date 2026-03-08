package com.example.moviles.model.dto;

import lombok.*;

// DTO que devuelve el token JWT tras un login exitoso
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {
    private String token;
    private String email;
    private String rol;
}
