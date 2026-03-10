package com.example.moviles;


import com.example.moviles.controller.MovilController;
import com.example.moviles.model.dto.MovilDetalleDTO;
import com.example.moviles.model.dto.MovilFiltroDTO;
import com.example.moviles.model.dto.MovilResumenDTO;
import com.example.moviles.service.MovilService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovilController.class)
@DisplayName("Tests integración ligera - MovilController (MockMvc)")
class MovilControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean  private MovilService movilService;

    private MovilResumenDTO resumenSamsung;
    private MovilDetalleDTO detalleSamsung;

    @BeforeEach
    void setUp() {
        resumenSamsung = new MovilResumenDTO(1L, "Samsung", "Galaxy S24 Ultra", 8, 12, 256, 1299.99);
        detalleSamsung = new MovilDetalleDTO(1L, "Samsung", "Galaxy S24 Ultra",
                "Snapdragon 8 Gen 3", 8, 3.3, 256, 6.8, "AMOLED",
                12, 16.2, 7.9, 0.86, 232, 200, 5000, true,
                1299.99, LocalDate.of(2024, 1, 17));
    }

    // ── GET /tendencia ───────────────────────────────────────────────

    @Test
    @DisplayName("GET /tendencia: devuelve 200 con lista de resúmenes")
    @WithMockUser
    void getTendencia_devuelve200() throws Exception {
        when(movilService.getTendencia()).thenReturn(List.of(resumenSamsung));

        mockMvc.perform(get("/api/v1/moviles/tendencia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].marca").value("Samsung"))
                .andExpect(jsonPath("$[0].modelo").value("Galaxy S24 Ultra"))
                .andExpect(jsonPath("$[0].precioActual").value(1299.99));
    }

    // ── GET /{id} ────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /{id}: devuelve 200 con detalle cuando existe")
    @WithMockUser
    void getDetalle_existente_devuelve200() throws Exception {
        when(movilService.getDetalle(1L)).thenReturn(Optional.of(detalleSamsung));

        mockMvc.perform(get("/api/v1/moviles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.marca").value("Samsung"))
                .andExpect(jsonPath("$.pantallaTecnologia").value("AMOLED"));
    }

    @Test
    @DisplayName("GET /{id}: devuelve 404 cuando no existe")
    @WithMockUser
    void getDetalle_noExistente_devuelve404() throws Exception {
        when(movilService.getDetalle(99L))
                .thenThrow(new EntityNotFoundException("Móvil no encontrado con id: 99"));

        mockMvc.perform(get("/api/v1/moviles/99"))
                .andExpect(status().isNotFound());
    }

    // ── GET /marcas ──────────────────────────────────────────────────

    @Test
    @DisplayName("GET /marcas: devuelve lista de marcas disponibles")
    @WithMockUser
    void getMarcas_devuelve200() throws Exception {
        when(movilService.getMarcasDisponibles()).thenReturn(List.of("Apple", "Samsung", "Xiaomi"));

        mockMvc.perform(get("/api/v1/moviles/marcas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Apple"))
                .andExpect(jsonPath("$[1]").value("Samsung"));
    }

    // ── POST /buscar ─────────────────────────────────────────────────

    @Test
    @DisplayName("POST /buscar: devuelve 200 con resultados válidos")
    @WithMockUser
    void buscar_filtroValido_devuelve200() throws Exception {
        MovilFiltroDTO filtro = new MovilFiltroDTO(500.0, 1500.0, null, null, null, null, null);
        when(movilService.buscarConFiltros(any())).thenReturn(List.of(resumenSamsung));

        mockMvc.perform(post("/api/v1/moviles/buscar")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filtro)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].marca").value("Samsung"));
    }

    @Test
    @DisplayName("POST /buscar: devuelve 400 si precio inválido")
    @WithMockUser
    void buscar_precioInvalido_devuelve400() throws Exception {
        MovilFiltroDTO filtro = new MovilFiltroDTO(null, null, null, null, null, null, null);
        when(movilService.buscarConFiltros(any()))
                .thenThrow(new IllegalArgumentException("El criterio de precio es obligatorio"));

        mockMvc.perform(post("/api/v1/moviles/buscar")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filtro)))
                .andExpect(status().isBadRequest());
    }

    // ── GET /comparar ────────────────────────────────────────────────

    @Test
    @DisplayName("GET /comparar: devuelve dos móviles para comparar")
    @WithMockUser
    void comparar_devuelve200() throws Exception {
        MovilDetalleDTO detalleApple = new MovilDetalleDTO(2L, "Apple", "iPhone 15 Pro",
                "A17 Pro", 6, 3.78, 256, 6.1, "OLED",
                8, 14.68, 7.12, 0.83, 187, 48, 3274, true,
                1199.99, LocalDate.of(2023, 9, 22));

        when(movilService.compararMoviles(1L, 2L)).thenReturn(List.of(detalleSamsung, detalleApple));

        mockMvc.perform(get("/api/v1/moviles/comparar")
                        .param("id1", "1")
                        .param("id2", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // ── POST / (ADMIN) ───────────────────────────────────────────────

    @Test
    @DisplayName("POST /: ADMIN puede crear un móvil → 201")
    @WithMockUser(roles = "ADMIN")
    void crear_comoAdmin_devuelve201() throws Exception {
        when(movilService.crear(any())).thenReturn(detalleSamsung);

        mockMvc.perform(post("/api/v1/moviles")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(detalleSamsung)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.marca").value("Samsung"));
    }

    @Test
    @DisplayName("POST /: GUEST no puede crear → 403")
    @WithMockUser(roles = "GUEST")
    void crear_comoGuest_devuelve403() throws Exception {
        mockMvc.perform(post("/api/v1/moviles")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(detalleSamsung)))
                .andExpect(status().isForbidden());
    }

    // ── DELETE /{id} (ADMIN) ─────────────────────────────────────────

    @Test
    @DisplayName("DELETE /{id}: ADMIN puede eliminar → 204")
    @WithMockUser(roles = "ADMIN")
    void eliminar_comoAdmin_devuelve204() throws Exception {
        doNothing().when(movilService).eliminar(1L);

        mockMvc.perform(delete("/api/v1/moviles/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /{id}: GUEST no puede eliminar → 403")
    @WithMockUser(roles = "GUEST")
    void eliminar_comoGuest_devuelve403() throws Exception {
        mockMvc.perform(delete("/api/v1/moviles/1").with(csrf()))
                .andExpect(status().isForbidden());
    }
}
