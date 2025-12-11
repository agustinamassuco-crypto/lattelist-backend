package com.example.LatteListBack.DTOs.UserDTOs;

import com.example.LatteListBack.Enums.EstadoUsuario;
import com.example.LatteListBack.Enums.TipoDeUsuario;

public record UsuarioDetailDTO(Long id,
                               String nombre,
                               String apellido,
                               String email,
                               TipoDeUsuario tipo,
                               String fotoPerfil,
                               String estadoUser
) {
}
