package com.example.moviles.service;

import com.example.moviles.model.dto.MovilDetalleDTO;
import com.example.moviles.model.dto.MovilFiltroDTO;
import com.example.moviles.model.dto.MovilResumenDTO;

import java.util.List;
import java.util.Optional;

public interface MovilService {

    List<MovilResumenDTO> getTendencia();

    Optional<MovilDetalleDTO> getDetalle(Long id);

    List<String> getMarcasDisponibles();

    List<MovilResumenDTO> buscarConFiltros(MovilFiltroDTO filtro);

    List<MovilDetalleDTO> compararMoviles(Long idMovil1, Long idMovil2);

    MovilDetalleDTO crear(MovilDetalleDTO dto);

    Optional<MovilDetalleDTO> actualizar(Long id, MovilDetalleDTO dto);

    void eliminar(Long id);
}