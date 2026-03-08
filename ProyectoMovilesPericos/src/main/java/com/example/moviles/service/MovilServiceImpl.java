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

@Service
@RequiredArgsConstructor
public class MovilServiceImpl implements MovilService {

    private final MovilRepository movilRepository;
    private final MovilMapper movilMapper;

    // -------------------------------------------------------
    // Página inicio: Top 5 tendencia
    // -------------------------------------------------------
    @Override
    public List<MovilResumenDTO> getTendencia() {
        return movilRepository.findTop5ByNumConsultas()
                .stream()
                .map(movilMapper::toResumen)
                .toList();
    }

    // -------------------------------------------------------
    // Detalle de un móvil (incrementa contador de consultas)
    // -------------------------------------------------------
    @Override
    @Transactional
    public MovilDetalleDTO getDetalle(Long id) {
        Movil movil = movilRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Móvil no encontrado con id: " + id));
        movilRepository.incrementarConsultas(id);
        return movilMapper.toDetalle(movil);
    }

    // -------------------------------------------------------
    // Marcas disponibles
    // -------------------------------------------------------
    @Override
    public List<String> getMarcasDisponibles() {
        return movilRepository.findMarcasDistintas();
    }

    // -------------------------------------------------------
    // Búsqueda con filtros (precio obligatorio)
    // -------------------------------------------------------
    @Override
    public List<MovilResumenDTO> buscarConFiltros(MovilFiltroDTO filtro) {
        validarFiltroPrecio(filtro);
        return movilRepository.findAll(MovilSpecification.conFiltros(filtro))
                .stream()
                .map(movilMapper::toResumen)
                .toList();
    }

    // -------------------------------------------------------
    // Comparativa de dos móviles
    // -------------------------------------------------------
    @Override
    public List<MovilDetalleDTO> compararMoviles(Long idMovil1, Long idMovil2) {
        List<Movil> moviles = movilRepository.findAllByIdIn(List.of(idMovil1, idMovil2));
        if (moviles.size() != 2) {
            throw new EntityNotFoundException("Uno o ambos móviles no encontrados.");
        }
        return moviles.stream().map(movilMapper::toDetalle).toList();
    }

    // -------------------------------------------------------
    // CRUD ADMIN
    // -------------------------------------------------------
    @Override
    @Transactional
    public MovilDetalleDTO crear(MovilDetalleDTO dto) {
        Movil movil = movilMapper.toEntity(dto);
        return movilMapper.toDetalle(movilRepository.save(movil));
    }

    @Override
    @Transactional
    public MovilDetalleDTO actualizar(Long id, MovilDetalleDTO dto) {
        Movil existente = movilRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Móvil no encontrado con id: " + id));

        existente.setMarca(dto.getMarca());
        existente.setModelo(dto.getModelo());
        existente.setProcesadorTipo(dto.getProcesadorTipo());
        existente.setProcesadorNucleos(dto.getProcesadorNucleos());
        existente.setProcesadorVelocidadGhz(dto.getProcesadorVelocidadGhz());
        existente.setAlmacenamientoGb(dto.getAlmacenamientoGb());
        existente.setPantallaPulgadas(dto.getPantallaPulgadas());
        existente.setPantallaTecnologia(dto.getPantallaTecnologia());
        existente.setRamGb(dto.getRamGb());
        existente.setDimensionAltoCm(dto.getDimensionAltoCm());
        existente.setDimensionAnchoCm(dto.getDimensionAnchoCm());
        existente.setDimensionGrosorCm(dto.getDimensionGrosorCm());
        existente.setPesoGr(dto.getPesoGr());
        existente.setCamaraMp(dto.getCamaraMp());
        existente.setBateriaMah(dto.getBateriaMah());
        existente.setNfc(dto.getNfc());
        existente.setPrecioActual(dto.getPrecioActual());
        existente.setFechaLanzamiento(dto.getFechaLanzamiento());

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

    // -------------------------------------------------------
    // Validación interna
    // -------------------------------------------------------
    private void validarFiltroPrecio(MovilFiltroDTO filtro) {
        if (filtro.getPrecioMin() == null || filtro.getPrecioMax() == null) {
            throw new IllegalArgumentException("El criterio de precio (min y max) es obligatorio en toda búsqueda.");
        }
        if (filtro.getPrecioMin() > filtro.getPrecioMax()) {
            throw new IllegalArgumentException("El precio mínimo no puede ser mayor que el precio máximo.");
        }
    }
}
