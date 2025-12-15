package com.example.LatteListBack.DTOs.CoffeDTOs;

import com.example.LatteListBack.Models.Cafe;

public class CafeFactory {

    private CafeFactory() {
    }

    public static CafeListDTO toListDTO(Cafe c, boolean abiertoAhora) {
        return new CafeListDTO(
                c.getId(),
                c.getNombre(),
                c.getDireccion(),
                c.getLatitud(),
                c.getLongitud(),
                abiertoAhora
        );
    }

    public static CafeDetailDTO toDetailDTO(Cafe c, CafeMetrics m, boolean abiertoAhora) {
        return new CafeDetailDTO(
                c.getId(),
                c.getOsmId(),
                c.getNombre(),
                c.getDireccion(),
                c.getLatitud(),
                c.getLongitud(),
                c.getTelefono(),
                c.getEmail(),
                c.getWebsite(),
                c.getCuisine(),
                c.getOpeningHours(),
                c.getTakeaway(),
                c.getDelivery(),
                c.getInternetAccess(),
                c.getOutdoorSeating(),
                abiertoAhora,
                m.puntuacion(),
                m.costoPromedio(),
                m.etiquetasTop3()
        );
    }

}

