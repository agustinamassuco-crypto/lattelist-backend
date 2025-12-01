package com.example.LatteListBack.DTOs.ReviewDTOs;

import java.util.List;

public record ReviewResponseDTO(
        Long id,
        Integer puntuacion,
        String comentario,
        String fecha,
        String costoPromedio,
        List<String> etiquetas,
        Long userId,//en el front cambiar a number
        Long cafeId,
        List<String> fotos
) {}

