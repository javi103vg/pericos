package com.example.moviles;

import com.example.moviles.configuration.mappers.MovilMapper;
import com.example.moviles.model.dto.MovilDetalleDTO;
import com.example.moviles.model.dto.MovilFiltroDTO;
import com.example.moviles.model.dto.MovilResumenDTO;
import com.example.moviles.models.Movil;
import com.example.moviles.repositories.MovilRepository;
import com.example.moviles.service.MovilServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitarios - MovilServiceImpl")
class MovilServiceImplTest {

    @Mock private MovilRepository movilRepository;
    @Mock private MovilMapper movilMapper;
    @InjectMocks private MovilServiceImpl movilService;

    private Movil movilSamsung;
    private Movil movilApple;
    private MovilResumenDTO resumenSamsung;
    private MovilDetalleDTO detalleSamsung;

    @BeforeEach
    void setUp() {
        movilSamsung = Movil.builder()
                .id(1L).marca("Samsung").modelo("Galaxy S24 Ultra")
                .procesadorTipo("Snapdragon 8 Gen 3").procesadorNucleos(8).procesadorVelocidadGhz(3.3)
                .almacenamientoGb(256).pantallaPulgadas(6.8).pantallaTecnologia("AMOLED")
                .ramGb(12).dimensionAltoCm(16.2).dimensionAnchoCm(7.9).dimensionGrosorCm(0.86)
                .pesoGr(232).camaraMp(200).bateriaMah(5000).nfc(true)
                .precioActual(1299.99).fechaLanzamiento(LocalDate.of(2024, 1, 17))
                .numConsultas(120L).build();

        movilApple = Movil.builder()
                .id(2L).marca("Apple").modelo("iPhone 15 Pro")
                .procesadorTipo("A17 Pro").procesadorNucleos(6).procesadorVelocidadGhz(3.78)
                .almacenamientoGb(256).pantallaPulgadas(6.1).pantallaTecnologia("OLED")
                .ramGb(8).dimensionAltoCm(14.68).dimensionAnchoCm(7.12).dimensionGrosorCm(0.83)
                .pesoGr(187).camaraMp(48).bateriaMah(3274).nfc(true)
                .precioActual(1199.99).fechaLanzamiento(LocalDate.of(2023, 9, 22))
                .numConsultas(98L).build();

        resumenSamsung = new MovilResumenDTO(1L, "Samsung", "Galaxy S24 Ultra", 8, 12, 256, 1299.99);

        detalleSamsung = new MovilDetalleDTO(1L, "Samsung", "Galaxy S24 Ultra",
                "Snapdragon 8 Gen 3", 8, 3.3, 256, 6.8, "AMOLED",
                12, 16.2, 7.9, 0.86, 232, 200, 5000, true,
                1299.99, LocalDate.of(2024, 1, 17));
    }

    // ── getTendencia ─────────────────────────────────────────────────

    @Test
    @DisplayName("getTendencia: devuelve lista de resúmenes mapeados")
    void getTendencia_devuelveListaResumen() {
        when(movilRepository.findTop5ByNumConsultas()).thenReturn(List.of(movilSamsung, movilApple));
        when(movilMapper.toResumen(movilSamsung)).thenReturn(Optional.of(resumenSamsung));
        when(movilMapper.toResumen(movilApple)).thenReturn(Optional.of(new MovilResumenDTO(2L, "Apple", "iPhone 15 Pro", 6, 8, 256, 1199.99)));

        List<MovilResumenDTO> resultado = movilService.getTendencia();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).marca()).isEqualTo("Samsung");
        verify(movilRepository).findTop5ByNumConsultas();
    }

    @Test
    @DisplayName("getTendencia: devuelve lista vacía si no hay móviles")
    void getTendencia_listaVacia() {
        when(movilRepository.findTop5ByNumConsultas()).thenReturn(List.of());
        assertThat(movilService.getTendencia()).isEmpty();
    }

    // ── getDetalle ───────────────────────────────────────────────────

    @Test
    @DisplayName("getDetalle: devuelve detalle e incrementa contador")
    void getDetalle_exitoso() {
        when(movilRepository.findById(1L)).thenReturn(Optional.of(movilSamsung));
        when(movilMapper.toDetalle(movilSamsung)).thenReturn(Optional.of(detalleSamsung));

        Optional<MovilDetalleDTO> resultado = movilService.getDetalle(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().marca()).isEqualTo("Samsung");
        verify(movilRepository).incrementarConsultas(1L);
    }

    @Test
    @DisplayName("getDetalle: lanza EntityNotFoundException si id no existe")
    void getDetalle_idNoExiste_lanzaExcepcion() {
        when(movilRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> movilService.getDetalle(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");

        verify(movilRepository, never()).incrementarConsultas(anyLong());
    }

    // ── getMarcasDisponibles ─────────────────────────────────────────

    @Test
    @DisplayName("getMarcasDisponibles: devuelve lista de marcas ordenadas")
    void getMarcasDisponibles_devuelveMarcas() {
        when(movilRepository.findMarcasDistintas()).thenReturn(List.of("Apple", "Samsung", "Xiaomi"));

        List<String> marcas = movilService.getMarcasDisponibles();

        assertThat(marcas).containsExactly("Apple", "Samsung", "Xiaomi");
    }

    // ── buscarConFiltros ─────────────────────────────────────────────

    @Test
    @DisplayName("buscarConFiltros: devuelve resultados con filtro válido")
    void buscarConFiltros_filtroValido_devuelveResultados() {
        MovilFiltroDTO filtro = new MovilFiltroDTO(500.0, 1500.0, null, null, null, null, null);
        when(movilRepository.findAll(any(Specification.class))).thenReturn(List.of(movilSamsung));
        when(movilMapper.toResumen(movilSamsung)).thenReturn(Optional.of(resumenSamsung));

        List<MovilResumenDTO> resultado = movilService.buscarConFiltros(filtro);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).precioActual()).isEqualTo(1299.99);
    }

    @Test
    @DisplayName("buscarConFiltros: lanza excepción si precioMin es null")
    void buscarConFiltros_sinPrecioMin_lanzaExcepcion() {
        MovilFiltroDTO filtro = new MovilFiltroDTO(null, 1000.0, null, null, null, null, null);

        assertThatThrownBy(() -> movilService.buscarConFiltros(filtro))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("precio");
    }

    @Test
    @DisplayName("buscarConFiltros: lanza excepción si precioMax es null")
    void buscarConFiltros_sinPrecioMax_lanzaExcepcion() {
        MovilFiltroDTO filtro = new MovilFiltroDTO(100.0, null, null, null, null, null, null);

        assertThatThrownBy(() -> movilService.buscarConFiltros(filtro))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("precio");
    }

    @Test
    @DisplayName("buscarConFiltros: lanza excepción si precioMin > precioMax")
    void buscarConFiltros_precioMinMayorQueMax_lanzaExcepcion() {
        MovilFiltroDTO filtro = new MovilFiltroDTO(1000.0, 500.0, null, null, null, null, null);

        assertThatThrownBy(() -> movilService.buscarConFiltros(filtro))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("mínimo");
    }

    // ── compararMoviles ──────────────────────────────────────────────

    @Test
    @DisplayName("compararMoviles: devuelve los dos móviles correctamente")
    void compararMoviles_devuelveDosDTOs() {
        MovilDetalleDTO detalleApple = new MovilDetalleDTO(2L, "Apple", "iPhone 15 Pro",
                "A17 Pro", 6, 3.78, 256, 6.1, "OLED",
                8, 14.68, 7.12, 0.83, 187, 48, 3274, true,
                1199.99, LocalDate.of(2023, 9, 22));

        when(movilRepository.findAllByIdIn(List.of(1L, 2L))).thenReturn(List.of(movilSamsung, movilApple));
        when(movilMapper.toDetalle(movilSamsung)).thenReturn(Optional.of(detalleSamsung));
        when(movilMapper.toDetalle(movilApple)).thenReturn(Optional.of(detalleApple));

        List<MovilDetalleDTO> resultado = movilService.compararMoviles(1L, 2L);

        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(MovilDetalleDTO::marca)
                .containsExactlyInAnyOrder("Samsung", "Apple");
    }

    @Test
    @DisplayName("compararMoviles: lanza excepción si algún id no existe")
    void compararMoviles_idInexistente_lanzaExcepcion() {
        when(movilRepository.findAllByIdIn(anyList())).thenReturn(List.of(movilSamsung)); // solo 1

        assertThatThrownBy(() -> movilService.compararMoviles(1L, 99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("no encontrados");
    }

    // ── crear ────────────────────────────────────────────────────────

    @Test
    @DisplayName("crear: guarda y devuelve el nuevo móvil")
    void crear_guardaMovilCorrectamente() {
        when(movilMapper.toEntity(detalleSamsung)).thenReturn(movilSamsung);
        when(movilRepository.save(movilSamsung)).thenReturn(movilSamsung);
        when(movilMapper.toDetalle(movilSamsung)).thenReturn(Optional.of(detalleSamsung));

        MovilDetalleDTO resultado = movilService.crear(detalleSamsung);

        assertThat(resultado.marca()).isEqualTo("Samsung");
        verify(movilRepository).save(movilSamsung);
    }

    // ── eliminar ─────────────────────────────────────────────────────

    @Test
    @DisplayName("eliminar: llama a deleteById cuando el móvil existe")
    void eliminar_movilExistente_eliminaCorrectamente() {
        when(movilRepository.existsById(1L)).thenReturn(true);

        movilService.eliminar(1L);

        verify(movilRepository).deleteById(1L);
    }

    @Test
    @DisplayName("eliminar: lanza excepción si el móvil no existe")
    void eliminar_movilNoExistente_lanzaExcepcion() {
        when(movilRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> movilService.eliminar(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");

        verify(movilRepository, never()).deleteById(anyLong());
    }
}
