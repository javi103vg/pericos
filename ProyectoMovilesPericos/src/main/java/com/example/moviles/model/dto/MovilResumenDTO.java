package com.example.moviles.model.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovilResumenDTO {

    private Long id;
    private String marca;
    private String modelo;
    private Integer procesadorNucleos;
    private Integer ramGb;
    private Integer almacenamientoGb;
    private Double precioActual;
}
