package com.example.moviles.model.dto;

public record MovilResumenDTO(
        Long id,
        String marca,
        String modelo,
        Integer procesadorNucleos,
        Integer ramGb,
        Integer almacenamientoGb,
        Double precioActual
) {}