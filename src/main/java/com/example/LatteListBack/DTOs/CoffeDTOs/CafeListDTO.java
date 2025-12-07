package com.example.LatteListBack.DTOs.CoffeDTOs;

public record CafeListDTO(
        Long id,
        String nombre,
        String direccion,
        Double latitud,
        Double longitud
        ) {}

