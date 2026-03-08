package com.example.moviles.controller;

import com.example.moviles.model.dto.MovilDetalleDTO;
import com.example.moviles.model.dto.MovilFiltroDTO;
import com.example.moviles.model.dto.MovilResumenDTO;
import com.example.moviles.service.MovilService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/moviles")
@RequiredArgsConstructor
public class MovilController {

    private final MovilService movilService;

    // =======================================================
    // PÚBLICO / INVITADO
    // =======================================================

    /**
     * GET /api/v1/moviles/tendencia
     * Página de inicio: los 5 móviles más consultados (info resumida)
     */
    @GetMapping("/tendencia")
    public ResponseEntity<List<MovilResumenDTO>> getTendencia() {
        return ResponseEntity.ok(movilService.getTendencia());
    }

    /**
     * GET /api/v1/moviles/{id}
     * Detalle completo de un móvil. Incrementa su contador de consultas.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MovilDetalleDTO> getDetalle(@PathVariable Long id) {
        return ResponseEntity.ok(movilService.getDetalle(id));
    }

    /**
     * GET /api/v1/moviles/marcas
     * Lista de marcas disponibles para el selector de búsqueda
     */
    @GetMapping("/marcas")
    public ResponseEntity<List<String>> getMarcas() {
        return ResponseEntity.ok(movilService.getMarcasDisponibles());
    }

    /**
     * POST /api/v1/moviles/buscar
     * Búsqueda con filtros. Precio (min/max) es OBLIGATORIO.
     * Opcionales: marca, ramMin, ramMax, nfc, pantallaTecnologia
     *
     * Ejemplo body:
     * {
     *   "precioMin": 200.0,
     *   "precioMax": 800.0,
     *   "marca": "Samsung",
     *   "ramMin": 8,
     *   "nfc": true,
     *   "pantallaTecnologia": "AMOLED"
     * }
     */
    @PostMapping("/buscar")
    public ResponseEntity<List<MovilResumenDTO>> buscar(@RequestBody MovilFiltroDTO filtro) {
        return ResponseEntity.ok(movilService.buscarConFiltros(filtro));
    }

    /**
     * GET /api/v1/moviles/comparar?id1=1&id2=2
     * Devuelve los datos completos de dos móviles para comparativa en columnas
     */
    @GetMapping("/comparar")
    public ResponseEntity<List<MovilDetalleDTO>> comparar(
            @RequestParam Long id1,
            @RequestParam Long id2) {
        return ResponseEntity.ok(movilService.compararMoviles(id1, id2));
    }

    // =======================================================
    // SOLO ADMIN
    // =======================================================

    /**
     * POST /api/v1/moviles
     * Crear nuevo móvil
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovilDetalleDTO> crear(@RequestBody MovilDetalleDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movilService.crear(dto));
    }

    /**
     * PUT /api/v1/moviles/{id}
     * Actualizar móvil existente
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovilDetalleDTO> actualizar(
            @PathVariable Long id,
            @RequestBody MovilDetalleDTO dto) {
        return ResponseEntity.ok(movilService.actualizar(id, dto));
    }

    /**
     * DELETE /api/v1/moviles/{id}
     * Eliminar móvil
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        movilService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
