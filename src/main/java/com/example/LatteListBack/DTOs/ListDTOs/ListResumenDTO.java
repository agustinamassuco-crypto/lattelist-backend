package com.example.LatteListBack.DTOs.ListDTOs;

import java.time.LocalDate;
import java.util.List;

public record ListResumenDTO(Long id,
                             String nombre,
                             LocalDate fechaCreacion,
                             List<Long> idCafes,
                             int cantidadCafes
) {}