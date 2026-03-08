package com.example.moviles.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "moviles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private String modelo;

    // --- Procesador ---
    @Column(name = "procesador_tipo")
    private String procesadorTipo;

    @Column(name = "procesador_nucleos")
    private Integer procesadorNucleos;

    @Column(name = "procesador_velocidad_ghz")
    private Double procesadorVelocidadGhz;

    // --- Almacenamiento (GB) ---
    @Column(name = "almacenamiento_gb")
    private Integer almacenamientoGb;

    // --- Pantalla ---
    @Column(name = "pantalla_pulgadas")
    private Double pantallaPulgadas;

    @Column(name = "pantalla_tecnologia")
    private String pantallaTecnologia;   // AMOLED, OLED, LCD, IPS...

    // --- RAM (GB) ---
    @Column(name = "ram_gb")
    private Integer ramGb;

    // --- Dimensiones (cm) ---
    @Column(name = "dimension_alto_cm")
    private Double dimensionAltoCm;

    @Column(name = "dimension_ancho_cm")
    private Double dimensionAnchoCm;

    @Column(name = "dimension_grosor_cm")
    private Double dimensionGrosorCm;

    // --- Peso (gr) ---
    @Column(name = "peso_gr")
    private Integer pesoGr;

    // --- Cámara (MP) ---
    @Column(name = "camara_mp")
    private Integer camaraMp;

    // --- Batería (mAh) ---
    @Column(name = "bateria_mah")
    private Integer bateriaMah;

    @Column(name = "nfc")
    private Boolean nfc;

    @Column(name = "precio_actual")
    private Double precioActual;

    @Column(name = "fecha_lanzamiento")
    private LocalDate fechaLanzamiento;

    // Contador de consultas para calcular tendencia (los 5 más consultados)
    @Column(name = "num_consultas")
    @Builder.Default
    private Long numConsultas = 0L;
}
