package com.example.LatteListBack.DTOs.AuthDTOs;

public record UsuarioAuthDTO(Long id,
                             String nombre,
                             String apellido,
                             String email,
                             String tipoUsuario) {
}
