package com.example.LatteListBack.DTOs.ListDTOs;

import com.example.LatteListBack.Models.ListaDeCafes;

public class ListFactory {

    public ListFactory() {
    }

    public static ListResumenDTO toListDTO(ListaDeCafes lista) {
        return new ListResumenDTO(
                lista.getId(),
                lista.getNombre(),
                lista.getFecha(),
                lista.getIdCafes(),
                lista.getIdCafes().size()
        );
    }
}
