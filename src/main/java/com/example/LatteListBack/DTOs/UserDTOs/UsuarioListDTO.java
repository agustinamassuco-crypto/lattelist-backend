package com.example.LatteListBack.DTOs.UserDTOs;

import com.example.LatteListBack.Enums.TipoDeUsuario;

public record UsuarioListDTO(Long id,
                             String nombre,
                             String apellido,
                             String email,
                             TipoDeUsuario tipo) {
}
