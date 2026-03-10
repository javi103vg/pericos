package com.example.moviles.service;

import com.example.moviles.configuration.mappers.MovilMapper;
import com.example.moviles.model.dto.MovilDetalleDTO;
import com.example.moviles.model.dto.MovilFiltroDTO;
import com.example.moviles.model.dto.MovilResumenDTO;
import com.example.moviles.models.Movil;
import com.example.moviles.repositories.MovilRepository;
import com.example.moviles.repositories.MovilSpecification;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovilServiceImpl implements MovilService {

    private final MovilRepository movilRepository;
    private final MovilMapper movilMapper;

    // ── Tendencia ────────────────────────────────────────────────────
    @Override
    public List<MovilResumenDTO> getTendencia() {
        return movilRepository.findTop5ByNumConsultas()
                .stream()
                .map(movilMapper::toResumen)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    // ── Detalle (incrementa contador) ────────────────────────────────
    @Override
    @Transactional
    public Optional<MovilDetalleDTO> getDetalle(Long id) {
        return movilRepository.findById(id)
                .map(movil -> {
                    movilRepository.incrementarConsultas(id);
                    return movilMapper.toDetalle(movil);
                })
                .orElseThrow(() -> new EntityNotFoundException("Móvil no encontrado con id: " + id));
    }

    // ── Marcas disponibles ───────────────────────────────────────────
    @Override
    public List<String> getMarcasDisponibles() {
        return movilRepository.findMarcasDistintas();
    }

    // ── Búsqueda con filtros ─────────────────────────────────────────
    @Override
    public List<MovilResumenDTO> buscarConFiltros(MovilFiltroDTO filtro) {
        validarFiltroPrecio(filtro);
        return movilRepository.findAll(MovilSpecification.conFiltros(filtro))
                .stream()
                .map(movilMapper::toResumen)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    // ── Comparativa ──────────────────────────────────────────────────
    @Override
    public List<MovilDetalleDTO> compararMoviles(Long idMovil1, Long idMovil2) {
        List<Movil> moviles = movilRepository.findAllByIdIn(List.of(idMovil1, idMovil2));
        if (moviles.size() != 2) {
            throw new EntityNotFoundException("Uno o ambos móviles no encontrados.");
        }
        return moviles.stream()
                .map(movilMapper::toDetalle)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    // ── CRUD ADMIN ───────────────────────────────────────────────────
    @Override
    @Transactional
    public MovilDetalleDTO crear(MovilDetalleDTO dto) {
        Movil movil = movilMapper.toEntity(dto);
        return movilMapper.toDetalle(movilRepository.save(movil))
                .orElseThrow(() -> new RuntimeException("Error al crear el móvil"));
    }

    @Override
    @Transactional
    public Optional<MovilDetalleDTO> actualizar(Long id, MovilDetalleDTO dto) {
        Movil existente = movilRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Móvil no encontrado con id: " + id));

        existente.setMarca(dto.marca());
        existente.setModelo(dto.modelo());
        existente.setProcesadorTipo(dto.procesadorTipo());
        existente.setProcesadorNucleos(dto.procesadorNucleos());
        existente.setProcesadorVelocidadGhz(dto.procesadorVelocidadGhz());
        existente.setAlmacenamientoGb(dto.almacenamientoGb());
        existente.setPantallaPulgadas(dto.pantallaPulgadas());
        existente.setPantallaTecnologia(dto.pantallaTecnologia());
        existente.setRamGb(dto.ramGb());
        existente.setDimensionAltoCm(dto.dimensionAltoCm());
        existente.setDimensionAnchoCm(dto.dimensionAnchoCm());
        existente.setDimensionGrosorCm(dto.dimensionGrosorCm());
        existente.setPesoGr(dto.pesoGr());
        existente.setCamaraMp(dto.camaraMp());
        existente.setBateriaMah(dto.bateriaMah());
        existente.setNfc(dto.nfc());
        existente.setPrecioActual(dto.precioActual());
        existente.setFechaLanzamiento(dto.fechaLanzamiento());

        return movilMapper.toDetalle(movilRepository.save(existente));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!movilRepository.existsById(id)) {
            throw new EntityNotFoundException("Móvil no encontrado con id: " + id);
        }
        movilRepository.deleteById(id);
    }

    // ── Validación interna ───────────────────────────────────────────
    private void validarFiltroPrecio(MovilFiltroDTO filtro) {
        if (filtro.precioMin() == null || filtro.precioMax() == null) {
            throw new IllegalArgumentException("El criterio de precio (min y max) es obligatorio en toda búsqueda.");
        }
        if (filtro.precioMin() > filtro.precioMax()) {
            throw new IllegalArgumentException("El precio mínimo no puede ser mayor que el precio máximo.");
        }
    }
}