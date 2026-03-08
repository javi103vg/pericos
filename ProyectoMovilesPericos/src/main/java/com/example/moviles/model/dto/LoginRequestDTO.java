package com.example.moviles.model.dto;

import lombok.*;

// DTO para recibir credenciales en el login
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {
    private String email;
    private String password;
}
