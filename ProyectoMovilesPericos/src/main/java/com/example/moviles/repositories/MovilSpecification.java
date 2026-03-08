package com.example.moviles.repositories;

import com.example.moviles.model.dto.MovilFiltroDTO;
import com.example.moviles.models.Movil;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MovilSpecification {

    public static Specification<Movil> conFiltros(MovilFiltroDTO filtro) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Precio: OBLIGATORIO
            if (filtro.getPrecioMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("precioActual"), filtro.getPrecioMin()));
            }
            if (filtro.getPrecioMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("precioActual"), filtro.getPrecioMax()));
            }

            // Marca (opcional)
            if (filtro.getMarca() != null && !filtro.getMarca().isBlank()) {
                predicates.add(cb.equal(
                        cb.lower(root.get("marca")),
                        filtro.getMarca().toLowerCase()
                ));
            }

            // RAM min/max (opcionales)
            if (filtro.getRamMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("ramGb"), filtro.getRamMin()));
            }
            if (filtro.getRamMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("ramGb"), filtro.getRamMax()));
            }

            // NFC (opcional)
            if (filtro.getNfc() != null) {
                predicates.add(cb.equal(root.get("nfc"), filtro.getNfc()));
            }

            // Tecnología de pantalla (opcional)
            if (filtro.getPantallaTecnologia() != null && !filtro.getPantallaTecnologia().isBlank()) {
                predicates.add(cb.equal(
                        cb.lower(root.get("pantallaTecnologia")),
                        filtro.getPantallaTecnologia().toLowerCase()
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
