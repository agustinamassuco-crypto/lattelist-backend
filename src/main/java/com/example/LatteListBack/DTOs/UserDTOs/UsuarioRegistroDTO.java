package com.example.LatteListBack.DTOs.UserDTOs;

public record UsuarioRegistroDTO(
        String nombre,
        String apellido,
        String email,
        String password,
        String fotoPerfil
) {}
