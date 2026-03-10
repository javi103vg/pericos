package com.example.moviles.model.dto;

import java.time.LocalDate;

public record MovilDetalleDTO(
        Long id,
        String marca,
        String modelo,
        String procesadorTipo,
        Integer procesadorNucleos,
        Double procesadorVelocidadGhz,
        Integer almacenamientoGb,
        Double pantallaPulgadas,
        String pantallaTecnologia,
        Integer ramGb,
        Double dimensionAltoCm,
        Double dimensionAnchoCm,
        Double dimensionGrosorCm,
        Integer pesoGr,
        Integer camaraMp,
        Integer bateriaMah,
        Boolean nfc,
        Double precioActual,
        LocalDate fechaLanzamiento
) {}