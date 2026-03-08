package com.example.moviles.populaters;

import com.example.moviles.models.Usuario;
import com.example.moviles.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Order(1)   // Se ejecuta antes que MovilPopulater
@RequiredArgsConstructor
public class UsuarioPopulater implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        crearUsuarioSiNoExiste("admin@moviles.com",  "admin1234",  Usuario.Rol.ADMIN);
        crearUsuarioSiNoExiste("guest@moviles.com",  "guest1234",  Usuario.Rol.GUEST);
        System.out.println("✅ Usuarios por defecto cargados correctamente.");
    }

    private void crearUsuarioSiNoExiste(String email, String password, Usuario.Rol rol) {
        if (!usuarioRepository.existsByEmail(email)) {
            usuarioRepository.save(Usuario.builder()
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .rol(rol)
                    .build());
        }
    }
}
