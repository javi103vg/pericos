package com.example.moviles.model.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovilFiltroDTO {

    // Obligatorio en toda búsqueda
    private Double precioMin;
    private Double precioMax;

    // Opcionales
    private String marca;
    private Integer ramMin;
    private Integer ramMax;
    private Boolean nfc;
    private String pantallaTecnologia;
}
