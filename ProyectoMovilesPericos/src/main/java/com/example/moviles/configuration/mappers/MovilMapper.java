package com.example.moviles.configuration.mappers;

import com.example.moviles.model.dto.MovilDetalleDTO;
import com.example.moviles.model.dto.MovilResumenDTO;
import com.example.moviles.models.Movil;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MovilMapper {

    public Optional<MovilResumenDTO> toResumen(Movil movil) {
        return Optional.ofNullable(movil).map(m -> new MovilResumenDTO(
                m.getId(),
                m.getMarca(),
                m.getModelo(),
                m.getProcesadorNucleos(),
                m.getRamGb(),
                m.getAlmacenamientoGb(),
                m.getPrecioActual()
        ));
    }

    public Optional<MovilDetalleDTO> toDetalle(Movil movil) {
        return Optional.ofNullable(movil).map(m -> new MovilDetalleDTO(
                m.getId(),
                m.getMarca(),
                m.getModelo(),
                m.getProcesadorTipo(),
                m.getProcesadorNucleos(),
                m.getProcesadorVelocidadGhz(),
                m.getAlmacenamientoGb(),
                m.getPantallaPulgadas(),
                m.getPantallaTecnologia(),
                m.getRamGb(),
                m.getDimensionAltoCm(),
                m.getDimensionAnchoCm(),
                m.getDimensionGrosorCm(),
                m.getPesoGr(),
                m.getCamaraMp(),
                m.getBateriaMah(),
                m.getNfc(),
                m.getPrecioActual(),
                m.getFechaLanzamiento()
        ));
    }

    public Movil toEntity(MovilDetalleDTO dto) {
        return Optional.ofNullable(dto).map(d -> Movil.builder()
                .marca(d.marca())
                .modelo(d.modelo())
                .procesadorTipo(d.procesadorTipo())
                .procesadorNucleos(d.procesadorNucleos())
                .procesadorVelocidadGhz(d.procesadorVelocidadGhz())
                .almacenamientoGb(d.almacenamientoGb())
                .pantallaPulgadas(d.pantallaPulgadas())
                .pantallaTecnologia(d.pantallaTecnologia())
                .ramGb(d.ramGb())
                .dimensionAltoCm(d.dimensionAltoCm())
                .dimensionAnchoCm(d.dimensionAnchoCm())
                .dimensionGrosorCm(d.dimensionGrosorCm())
                .pesoGr(d.pesoGr())
                .camaraMp(d.camaraMp())
                .bateriaMah(d.bateriaMah())
                .nfc(d.nfc())
                .precioActual(d.precioActual())
                .fechaLanzamiento(d.fechaLanzamiento())
                .build()
        ).orElseThrow(() -> new IllegalArgumentException("El DTO no puede ser nulo"));
    }
}