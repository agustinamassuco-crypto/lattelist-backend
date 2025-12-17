package com.example.LatteListBack.DTOs.ListDTOs;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.Set;

public record ListResumenDTO(Long id,
                             String nombre,
                             String fechaCreacion,
                             Set<Long> idCafes,
                             Set<Long> idCafesVisitados,
                             Integer cafeTotal,
                             Boolean publica,
                             String userNombre,
                             Long idUser
) {}