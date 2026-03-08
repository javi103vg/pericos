package com.example.moviles.populaters;

import com.example.moviles.models.Movil;
import com.example.moviles.repositories.MovilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MovilPopulater implements CommandLineRunner {

    private final MovilRepository movilRepository;

    @Override
    public void run(String... args) {
        if (movilRepository.count() > 0) return; // Solo poblar si está vacío

        List<Movil> moviles = List.of(
            Movil.builder()
                .marca("Samsung").modelo("Galaxy S24 Ultra")
                .procesadorTipo("Snapdragon 8 Gen 3").procesadorNucleos(8).procesadorVelocidadGhz(3.3)
                .almacenamientoGb(256).pantallaPulgadas(6.8).pantallaTecnologia("AMOLED")
                .ramGb(12).dimensionAltoCm(16.2).dimensionAnchoCm(7.9).dimensionGrosorCm(0.86)
                .pesoGr(232).camaraMp(200).bateriaMah(5000).nfc(true)
                .precioActual(1299.99).fechaLanzamiento(LocalDate.of(2024, 1, 17))
                .numConsultas(120L).build(),

            Movil.builder()
                .marca("Apple").modelo("iPhone 15 Pro")
                .procesadorTipo("A17 Pro").procesadorNucleos(6).procesadorVelocidadGhz(3.78)
                .almacenamientoGb(256).pantallaPulgadas(6.1).pantallaTecnologia("OLED")
                .ramGb(8).dimensionAltoCm(14.68).dimensionAnchoCm(7.12).dimensionGrosorCm(0.83)
                .pesoGr(187).camaraMp(48).bateriaMah(3274).nfc(true)
                .precioActual(1199.99).fechaLanzamiento(LocalDate.of(2023, 9, 22))
                .numConsultas(98L).build(),

            Movil.builder()
                .marca("Xiaomi").modelo("14 Pro")
                .procesadorTipo("Snapdragon 8 Gen 3").procesadorNucleos(8).procesadorVelocidadGhz(3.3)
                .almacenamientoGb(512).pantallaPulgadas(6.73).pantallaTecnologia("AMOLED")
                .ramGb(16).dimensionAltoCm(16.1).dimensionAnchoCm(7.5).dimensionGrosorCm(0.85)
                .pesoGr(223).camaraMp(50).bateriaMah(4880).nfc(true)
                .precioActual(899.99).fechaLanzamiento(LocalDate.of(2023, 10, 26))
                .numConsultas(75L).build(),

            Movil.builder()
                .marca("Google").modelo("Pixel 8 Pro")
                .procesadorTipo("Tensor G3").procesadorNucleos(9).procesadorVelocidadGhz(2.91)
                .almacenamientoGb(128).pantallaPulgadas(6.7).pantallaTecnologia("OLED")
                .ramGb(12).dimensionAltoCm(16.25).dimensionAnchoCm(7.62).dimensionGrosorCm(0.88)
                .pesoGr(213).camaraMp(50).bateriaMah(5050).nfc(true)
                .precioActual(999.99).fechaLanzamiento(LocalDate.of(2023, 10, 12))
                .numConsultas(60L).build(),

            Movil.builder()
                .marca("OnePlus").modelo("12")
                .procesadorTipo("Snapdragon 8 Gen 3").procesadorNucleos(8).procesadorVelocidadGhz(3.3)
                .almacenamientoGb(256).pantallaPulgadas(6.82).pantallaTecnologia("AMOLED")
                .ramGb(12).dimensionAltoCm(16.4).dimensionAnchoCm(7.57).dimensionGrosorCm(0.94)
                .pesoGr(220).camaraMp(50).bateriaMah(5400).nfc(true)
                .precioActual(799.99).fechaLanzamiento(LocalDate.of(2024, 1, 23))
                .numConsultas(45L).build(),

            Movil.builder()
                .marca("Samsung").modelo("Galaxy A55")
                .procesadorTipo("Exynos 1480").procesadorNucleos(8).procesadorVelocidadGhz(2.75)
                .almacenamientoGb(128).pantallaPulgadas(6.6).pantallaTecnologia("AMOLED")
                .ramGb(8).dimensionAltoCm(16.18).dimensionAnchoCm(7.77).dimensionGrosorCm(0.82)
                .pesoGr(213).camaraMp(50).bateriaMah(5000).nfc(true)
                .precioActual(449.99).fechaLanzamiento(LocalDate.of(2024, 3, 11))
                .numConsultas(30L).build()
        );

        movilRepository.saveAll(moviles);
        System.out.println("✅ Datos de móviles cargados correctamente.");
    }
}
