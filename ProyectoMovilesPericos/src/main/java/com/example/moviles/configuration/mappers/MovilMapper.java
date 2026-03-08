package com.example.moviles.configuration.mappers;

import com.example.moviles.model.dto.MovilDetalleDTO;
import com.example.moviles.model.dto.MovilResumenDTO;
import com.example.moviles.models.Movil;
import org.springframework.stereotype.Component;

@Component
public class MovilMapper {

    public MovilResumenDTO toResumen(Movil movil) {
        return MovilResumenDTO.builder()
                .id(movil.getId())
                .marca(movil.getMarca())
                .modelo(movil.getModelo())
                .procesadorNucleos(movil.getProcesadorNucleos())
                .ramGb(movil.getRamGb())
                .almacenamientoGb(movil.getAlmacenamientoGb())
                .precioActual(movil.getPrecioActual())
                .build();
    }

    public MovilDetalleDTO toDetalle(Movil movil) {
        return MovilDetalleDTO.builder()
                .id(movil.getId())
                .marca(movil.getMarca())
                .modelo(movil.getModelo())
                .procesadorTipo(movil.getProcesadorTipo())
                .procesadorNucleos(movil.getProcesadorNucleos())
                .procesadorVelocidadGhz(movil.getProcesadorVelocidadGhz())
                .almacenamientoGb(movil.getAlmacenamientoGb())
                .pantallaPulgadas(movil.getPantallaPulgadas())
                .pantallaTecnologia(movil.getPantallaTecnologia())
                .ramGb(movil.getRamGb())
                .dimensionAltoCm(movil.getDimensionAltoCm())
                .dimensionAnchoCm(movil.getDimensionAnchoCm())
                .dimensionGrosorCm(movil.getDimensionGrosorCm())
                .pesoGr(movil.getPesoGr())
                .camaraMp(movil.getCamaraMp())
                .bateriaMah(movil.getBateriaMah())
                .nfc(movil.getNfc())
                .precioActual(movil.getPrecioActual())
                .fechaLanzamiento(movil.getFechaLanzamiento())
                .build();
    }

    public Movil toEntity(MovilDetalleDTO dto) {
        return Movil.builder()
                .marca(dto.getMarca())
                .modelo(dto.getModelo())
                .procesadorTipo(dto.getProcesadorTipo())
                .procesadorNucleos(dto.getProcesadorNucleos())
                .procesadorVelocidadGhz(dto.getProcesadorVelocidadGhz())
                .almacenamientoGb(dto.getAlmacenamientoGb())
                .pantallaPulgadas(dto.getPantallaPulgadas())
                .pantallaTecnologia(dto.getPantallaTecnologia())
                .ramGb(dto.getRamGb())
                .dimensionAltoCm(dto.getDimensionAltoCm())
                .dimensionAnchoCm(dto.getDimensionAnchoCm())
                .dimensionGrosorCm(dto.getDimensionGrosorCm())
                .pesoGr(dto.getPesoGr())
                .camaraMp(dto.getCamaraMp())
                .bateriaMah(dto.getBateriaMah())
                .nfc(dto.getNfc())
                .precioActual(dto.getPrecioActual())
                .fechaLanzamiento(dto.getFechaLanzamiento())
                .build();
    }
}
