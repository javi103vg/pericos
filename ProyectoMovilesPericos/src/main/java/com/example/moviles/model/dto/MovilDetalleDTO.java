package com.example.moviles.model.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovilDetalleDTO {

    private Long id;
    private String marca;
    private String modelo;

    // Procesador
    private String procesadorTipo;
    private Integer procesadorNucleos;
    private Double procesadorVelocidadGhz;

    // Almacenamiento
    private Integer almacenamientoGb;

    // Pantalla
    private Double pantallaPulgadas;
    private String pantallaTecnologia;

    // RAM
    private Integer ramGb;

    // Dimensiones
    private Double dimensionAltoCm;
    private Double dimensionAnchoCm;
    private Double dimensionGrosorCm;

    // Peso
    private Integer pesoGr;

    // Cámara
    private Integer camaraMp;

    // Batería
    private Integer bateriaMah;

    private Boolean nfc;
    private Double precioActual;
    private LocalDate fechaLanzamiento;
}
