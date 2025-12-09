package com.example.LatteListBack.DTOs.LikeReviewDTO;

import com.example.LatteListBack.Enums.TipoReaccion;

public record LikeReviewDTO(
        Long id,
        Long usuarioId,
        Long reviewId,
        TipoReaccion tipo
) {}
