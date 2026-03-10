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

    // ── PÚBLICO ──────────────────────────────────────────────────────

    @GetMapping("/tendencia")
    public ResponseEntity<List<MovilResumenDTO>> getTendencia() {
        return ResponseEntity.ok(movilService.getTendencia());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovilDetalleDTO> getDetalle(@PathVariable Long id) {
        return movilService.getDetalle(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/marcas")
    public ResponseEntity<List<String>> getMarcas() {
        return ResponseEntity.ok(movilService.getMarcasDisponibles());
    }

    @PostMapping("/buscar")
    public ResponseEntity<List<MovilResumenDTO>> buscar(@RequestBody MovilFiltroDTO filtro) {
        return ResponseEntity.ok(movilService.buscarConFiltros(filtro));
    }

    @GetMapping("/comparar")
    public ResponseEntity<List<MovilDetalleDTO>> comparar(
            @RequestParam Long id1,
            @RequestParam Long id2) {
        return ResponseEntity.ok(movilService.compararMoviles(id1, id2));
    }

    // ── SOLO ADMIN ───────────────────────────────────────────────────

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovilDetalleDTO> crear(@RequestBody MovilDetalleDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movilService.crear(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovilDetalleDTO> actualizar(
            @PathVariable Long id,
            @RequestBody MovilDetalleDTO dto) {
        return movilService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        movilService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}