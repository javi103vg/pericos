package com.example.moviles.service;

import com.example.moviles.model.dto.MovilDetalleDTO;
import com.example.moviles.model.dto.MovilFiltroDTO;
import com.example.moviles.model.dto.MovilResumenDTO;

import java.util.List;

public interface MovilService {

    // --- Página de inicio: Top 5 tendencia ---
    List<MovilResumenDTO> getTendencia();

    // --- Detalle de un móvil concreto (incrementa contador) ---
    MovilDetalleDTO getDetalle(Long id);

    // --- Marcas disponibles para el selector inicial ---
    List<String> getMarcasDisponibles();

    // --- Búsqueda con filtros (precio obligatorio + opcionales) ---
    List<MovilResumenDTO> buscarConFiltros(MovilFiltroDTO filtro);

    // --- Comparativa de dos móviles ---
    List<MovilDetalleDTO> compararMoviles(Long idMovil1, Long idMovil2);

    // --- CRUD para ADMIN ---
    MovilDetalleDTO crear(MovilDetalleDTO dto);
    MovilDetalleDTO actualizar(Long id, MovilDetalleDTO dto);
    void eliminar(Long id);
}
