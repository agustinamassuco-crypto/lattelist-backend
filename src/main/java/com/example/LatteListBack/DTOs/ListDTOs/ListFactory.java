package com.example.LatteListBack.DTOs.ListDTOs;

import com.example.LatteListBack.Models.ListaDeCafes;

import java.util.ArrayList;

public class ListFactory {

    public ListFactory() {
    }


    public static ListResumenDTO toListDTO(ListaDeCafes lista) {
        return new ListResumenDTO(
                lista.getId(),
                lista.getNombre(),
                lista.getFecha(),
                new ArrayList<>(lista.getIdCafes()),
                lista.getIdCafes().size()
        );
    }

}
