package com.example.moviles;

import com.example.moviles.model.dto.LoginRequestDTO;
import com.example.moviles.model.dto.MovilDetalleDTO;
import com.example.moviles.model.dto.MovilFiltroDTO;
import com.example.moviles.models.Movil;
import com.example.moviles.repositories.MovilRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Tests integración completa - API REST Móviles")
class MovilIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private MovilRepository movilRepository;

    private String adminToken;
    private Long movilId;

    @BeforeEach
    void setUp() throws Exception {
        // Obtener token de admin
        LoginRequestDTO loginAdmin = new LoginRequestDTO("admin@moviles.com", "admin1234");
        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginAdmin)))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        adminToken = objectMapper.readTree(body).get("token").asText();

        // Tomar el id del primer móvil del populater
        movilId = movilRepository.findAll().stream()
                .findFirst()
                .map(Movil::getId)
                .orElseThrow();
    }

    // ── Auth ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("Login admin correcto devuelve token JWT")
    void login_admin_devuelveToken() throws Exception {
        LoginRequestDTO login = new LoginRequestDTO("admin@moviles.com", "admin1234");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.email").value("admin@moviles.com"))
                .andExpect(jsonPath("$.rol").value("ADMIN"));
    }

    @Test
    @DisplayName("Login con credenciales incorrectas devuelve 401")
    void login_credencialesIncorrectas_devuelve401() throws Exception {
        LoginRequestDTO login = new LoginRequestDTO("admin@moviles.com", "wrongpassword");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized());
    }

    // ── Endpoints públicos ───────────────────────────────────────────

    @Test
    @DisplayName("GET /tendencia: accesible sin token, devuelve 5 móviles")
    void getTendencia_sinToken_devuelve200() throws Exception {
        mockMvc.perform(get("/api/v1/moviles/tendencia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    @DisplayName("GET /marcas: accesible sin token, devuelve lista de marcas")
    void getMarcas_sinToken_devuelve200() throws Exception {
        mockMvc.perform(get("/api/v1/moviles/marcas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("GET /{id}: devuelve detalle e incrementa contador de consultas")
    void getDetalle_existente_devuelveDetalleEIncrementaContador() throws Exception {
        long consultasAntes = movilRepository.findById(movilId).orElseThrow().getNumConsultas();

        mockMvc.perform(get("/api/v1/moviles/" + movilId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(movilId));

        long consultasDespues = movilRepository.findById(movilId).orElseThrow().getNumConsultas();
        assertThat(consultasDespues).isEqualTo(consultasAntes + 1);
    }

    @Test
    @DisplayName("GET /{id}: devuelve 404 si el móvil no existe")
    void getDetalle_noExistente_devuelve404() throws Exception {
        mockMvc.perform(get("/api/v1/moviles/99999"))
                .andExpect(status().isNotFound());
    }

    // ── Búsqueda con filtros ─────────────────────────────────────────

    @Test
    @DisplayName("POST /buscar: devuelve móviles dentro del rango de precio")
    void buscar_conPrecio_devuelveResultados() throws Exception {
        MovilFiltroDTO filtro = new MovilFiltroDTO(400.0, 1500.0, null, null, null, null, null);

        mockMvc.perform(post("/api/v1/moviles/buscar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filtro)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("POST /buscar: devuelve 400 si falta precio")
    void buscar_sinPrecio_devuelve400() throws Exception {
        MovilFiltroDTO filtro = new MovilFiltroDTO(null, null, null, null, null, null, null);

        mockMvc.perform(post("/api/v1/moviles/buscar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filtro)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /buscar: filtro por marca devuelve solo esa marca")
    void buscar_porMarca_devuelveSoloEsaMarca() throws Exception {
        MovilFiltroDTO filtro = new MovilFiltroDTO(0.0, 9999.0, "Samsung", null, null, null, null);

        mockMvc.perform(post("/api/v1/moviles/buscar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filtro)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].marca").value(
                        org.hamcrest.Matchers.everyItem(org.hamcrest.Matchers.is("Samsung"))));
    }

    // ── Comparativa ──────────────────────────────────────────────────

    @Test
    @DisplayName("GET /comparar: devuelve dos móviles en columnas")
    void comparar_dosMovilesExistentes_devuelve200() throws Exception {
        List<Movil> moviles = movilRepository.findAll();
        Long id1 = moviles.get(0).getId();
        Long id2 = moviles.get(1).getId();

        mockMvc.perform(get("/api/v1/moviles/comparar")
                        .param("id1", id1.toString())
                        .param("id2", id2.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // ── CRUD ADMIN ───────────────────────────────────────────────────

    @Test
    @DisplayName("POST /: ADMIN puede crear un nuevo móvil → 201")
    void crear_comoAdmin_devuelve201() throws Exception {
        MovilDetalleDTO nuevo = new MovilDetalleDTO(null, "Motorola", "Edge 50 Pro",
                "Snapdragon 7s Gen 2", 8, 2.4, 256, 6.7, "AMOLED",
                12, 16.1, 7.4, 0.8, 180, 50, 4500, true,
                499.99, LocalDate.of(2024, 5, 1));

        mockMvc.perform(post("/api/v1/moviles")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.marca").value("Motorola"))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    @DisplayName("POST /: sin token devuelve 403")
    void crear_sinToken_devuelve403() throws Exception {
        MovilDetalleDTO nuevo = new MovilDetalleDTO(null, "Test", "Test",
                null, null, null, null, null, null,
                null, null, null, null, null, null, null, null,
                null, null);

        mockMvc.perform(post("/api/v1/moviles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("PUT /{id}: ADMIN puede actualizar un móvil → 200")
    void actualizar_comoAdmin_devuelve200() throws Exception {
        MovilDetalleDTO actualizado = new MovilDetalleDTO(movilId, "Samsung", "Galaxy S24 Ultra EDITED",
                "Snapdragon 8 Gen 3", 8, 3.3, 512, 6.8, "AMOLED",
                12, 16.2, 7.9, 0.86, 232, 200, 5000, true,
                1199.99, LocalDate.of(2024, 1, 17));

        mockMvc.perform(put("/api/v1/moviles/" + movilId)
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modelo").value("Galaxy S24 Ultra EDITED"));
    }

    @Test
    @DisplayName("DELETE /{id}: ADMIN puede eliminar un móvil → 204")
    void eliminar_comoAdmin_devuelve204() throws Exception {
        // Crear uno para no romper otros tests
        Movil temporal = movilRepository.save(Movil.builder()
                .marca("Temporal").modelo("Para borrar")
                .precioActual(100.0).numConsultas(0L).build());

        mockMvc.perform(delete("/api/v1/moviles/" + temporal.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());

        assertThat(movilRepository.existsById(temporal.getId())).isFalse();
    }
}
