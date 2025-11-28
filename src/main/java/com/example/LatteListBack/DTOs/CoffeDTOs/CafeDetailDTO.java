package com.example.LatteListBack.DTOs.CoffeDTOs;

import com.example.LatteListBack.Enums.CostoPromedio;
import com.example.LatteListBack.Enums.Etiquetas;

import java.util.Set;

public record CafeDetailDTO(
        Long id,
        Long osmId,
        String nombre,
        String direccion,
        Double latitud,
        Double longitud,

        String telefono,
        String email,
        String website,

        String cuisine,
        String opening_hours,

        Boolean takeaway,
        Boolean delivery,
        Boolean internet_access,
        Boolean outdoor_seating,

        Integer puntuacion,
        CostoPromedio costoPromedio,
        Set<Etiquetas> etiquetasTop3
) {}

