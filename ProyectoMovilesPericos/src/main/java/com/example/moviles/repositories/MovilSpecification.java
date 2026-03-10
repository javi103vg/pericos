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
            if (filtro.precioMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("precioActual"), filtro.precioMin()));
            }
            if (filtro.precioMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("precioActual"), filtro.precioMax()));
            }

            // Marca (opcional)
            if (filtro.marca() != null && !filtro.marca().isBlank()) {
                predicates.add(cb.equal(
                        cb.lower(root.get("marca")),
                        filtro.marca().toLowerCase()
                ));
            }

            // RAM min/max (opcionales)
            if (filtro.ramMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("ramGb"), filtro.ramMin()));
            }
            if (filtro.ramMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("ramGb"), filtro.ramMax()));
            }

            // NFC (opcional)
            if (filtro.nfc() != null) {
                predicates.add(cb.equal(root.get("nfc"), filtro.nfc()));
            }

            // Tecnología de pantalla (opcional)
            if (filtro.pantallaTecnologia() != null && !filtro.pantallaTecnologia().isBlank()) {
                predicates.add(cb.equal(
                        cb.lower(root.get("pantallaTecnologia")),
                        filtro.pantallaTecnologia().toLowerCase()
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}