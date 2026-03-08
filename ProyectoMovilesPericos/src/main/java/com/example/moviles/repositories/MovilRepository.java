package com.example.moviles.repositories;

import com.example.moviles.models.Movil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovilRepository extends JpaRepository<Movil, Long>,
        JpaSpecificationExecutor<Movil> {

    // Marcas distintas disponibles
    @Query("SELECT DISTINCT m.marca FROM Movil m ORDER BY m.marca ASC")
    List<String> findMarcasDistintas();

    // Top 5 más consultados (tendencia)
    @Query("SELECT m FROM Movil m ORDER BY m.numConsultas DESC LIMIT 5")
    List<Movil> findTop5ByNumConsultas();

    // Incrementar contador de consultas
    @Modifying
    @Query("UPDATE Movil m SET m.numConsultas = m.numConsultas + 1 WHERE m.id = :id")
    void incrementarConsultas(@Param("id") Long id);

    // Obtener los dos móviles para comparativa
    List<Movil> findAllByIdIn(List<Long> ids);
}
