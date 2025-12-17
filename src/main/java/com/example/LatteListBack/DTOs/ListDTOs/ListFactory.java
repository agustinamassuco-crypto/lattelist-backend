package com.example.LatteListBack.DTOs.ListDTOs;

import com.example.LatteListBack.Models.ListaDeCafes;

import java.util.ArrayList;
import java.util.HashSet;

public class ListFactory {

    public ListFactory() {
    }


    public static ListResumenDTO toListDTO(ListaDeCafes lista) {
        String ownerName = "";
        Long ownerId = null;

        if (lista.getUsuario() != null) {
            ownerName = lista.getUsuario().getNombre() + " " + lista.getUsuario().getApellido();
            ownerId = lista.getUsuario().getId();
        }

        return new ListResumenDTO(
                lista.getId(),
                lista.getNombre(),
                lista.getFecha().toString(),
                lista.getIdCafes() != null ? lista.getIdCafes() : new HashSet<>(),
                lista.getIdCafesVisitados() != null ? lista.getIdCafesVisitados() : new HashSet<>(),
                lista.getIdCafes().size(),
                lista.getPublica(),
                ownerName,
                ownerId
        );
    }
}
