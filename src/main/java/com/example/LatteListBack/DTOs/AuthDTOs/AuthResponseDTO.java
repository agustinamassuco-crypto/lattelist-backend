package com.example.LatteListBack.DTOs.AuthDTOs;

import com.example.LatteListBack.Enums.TipoDeUsuario;

public record AuthResponseDTO(
        String token,
        Long id,
        String nombre,
        String apellido,
        String email,
        TipoDeUsuario tipoDeUsuario,
        String fotoPerfil,
        String estado
) {}
