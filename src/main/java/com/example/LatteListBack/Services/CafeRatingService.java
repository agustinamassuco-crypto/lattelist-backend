package com.example.LatteListBack.Services;

import com.example.LatteListBack.DTOs.CoffeDTOs.CafeMetrics;
import com.example.LatteListBack.Enums.CostoPromedio;
import com.example.LatteListBack.Enums.EstadoReview;
import com.example.LatteListBack.Enums.Etiquetas;
import com.example.LatteListBack.Models.Cafe;
import com.example.LatteListBack.Models.Review;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CafeRatingService {

    public CafeMetrics calcular(Cafe cafe) {

        Integer puntuacion = calcularPuntuacion(cafe);
        CostoPromedio costo = calcularCosto(cafe);
        Set<Etiquetas> top = calcularEtiquetas(cafe);

        return new CafeMetrics(puntuacion, costo, top);
    }

    private Integer calcularPuntuacion(Cafe cafe) {
        List<Integer> puntuaciones = cafe.getResenas().stream()
                .filter(r -> r.getEstado() == EstadoReview.ACTIVA)
                .map(Review::getPuntuacion)
                .toList();

        if (puntuaciones.isEmpty()) {
            return null;
        }

        double promedio = puntuaciones.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);

        return (int) Math.round(promedio);
    }

    private CostoPromedio calcularCosto(Cafe cafe) {
        return cafe.getResenas().stream()
                .filter(r -> r.getCostoPromedio() != null)
                .collect(Collectors.groupingBy(Review::getCostoPromedio, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private Set<Etiquetas> calcularEtiquetas(Cafe cafe) {

        List<Etiquetas> top3 = cafe.getResenas().stream()
                .flatMap(r -> r.getEtiquetas().stream())
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<Etiquetas, Long>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .toList();

        return Set.copyOf(top3);
    }
}
