package com.example.LatteListBack.DTOs.CoffeDTOs;

import com.example.LatteListBack.Enums.CostoPromedio;
import com.example.LatteListBack.Enums.Etiquetas;

import java.util.Set;

public record CafeMetrics(
        Double puntuacion,
        CostoPromedio costoPromedio,
        Set<Etiquetas> etiquetasTop3
) {}
