package com.example.LatteListBack.Mappers;

import com.example.LatteListBack.Models.Cafe;
import org.springframework.stereotype.Component;

@Component
public class CafeMapper {

    public void copiarDatosDesdeApi(Cafe destino, Cafe fuente) {
        destino.setNombre(fuente.getNombre());
        destino.setDireccion(fuente.getDireccion());
        destino.setLatitud(fuente.getLatitud());
        destino.setLongitud(fuente.getLongitud());
        destino.setTelefono(fuente.getTelefono());
        destino.setEmail(fuente.getEmail());
        destino.setWebsite(fuente.getWebsite());
        destino.setDelivery(fuente.getDelivery());
        destino.setTakeaway(fuente.getTakeaway());
        destino.setOutdoorSeating(fuente.getOutdoorSeating());
        destino.setInternetAccess(fuente.getInternetAccess());
        destino.setCuisine(fuente.getCuisine());
        destino.setOpeningHours(fuente.getOpeningHours());
        destino.setOsmId(fuente.getOsmId());
    }
}

