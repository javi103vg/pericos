package com.example.moviles.service;

import com.example.moviles.jwts.JwtService;
import com.example.moviles.model.dto.LoginRequestDTO;
import com.example.moviles.model.dto.LoginResponseDTO;
import com.example.moviles.models.Usuario;
import com.example.moviles.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public LoginResponseDTO login(LoginRequestDTO request) {
        // Lanza excepción automáticamente si las credenciales son incorrectas
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtService.generateToken(usuario);

        // Los records no tienen builder, se instancian directamente con el constructor
        return new LoginResponseDTO(token, usuario.getEmail(), usuario.getRol().name());
    }
}