package com.example.LatteListBack.Mappers;

import com.example.LatteListBack.DTOs.GeoApifyDTOs.Feature;
import com.example.LatteListBack.DTOs.GeoApifyDTOs.PropertiesDTO;
import com.example.LatteListBack.Models.Cafe;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public class GeoapifyCafeMapper {


    public Cafe mapFeatureToCafe(Feature feature) {
        PropertiesDTO props = feature.getProperties();
        Cafe cafe = new Cafe();

        Map<String, Object> rawTags = this.getRawTags(props);

        mapDatosBase(cafe, props);
        mapContacto(cafe, props, rawTags);
        mapServicios(cafe, props, rawTags);
        mapOperacion(cafe, props, rawTags);

        return cafe;
    }


    private Map<String, Object> getRawTags(PropertiesDTO props) {
        return props.getDatasource() != null ?
                props.getDatasource().getRaw() :
                Collections.emptyMap();
    }


    private void mapDatosBase(Cafe cafe, PropertiesDTO props) {
        cafe.setNombre(props.getName());

        String direccion;
        if (props.getStreet() != null && props.getHousenumber() != null) {
            direccion = props.getStreet() + " " + props.getHousenumber();
        } else {
            direccion = props.getFormatted();
        }
        cafe.setDireccion(direccion);

        cafe.setLatitud(props.getLat());
        cafe.setLongitud(props.getLon());
    }




    private void mapContacto(Cafe cafe, PropertiesDTO props, Map<String, Object> rawTags) {
        if (rawTags.containsKey("phone")) {
            cafe.setTelefono(rawTags.get("phone").toString());
        } else if (props.getContact() != null) {
            cafe.setTelefono(props.getContact().getPhone());
        }

        if (rawTags.containsKey("email")) {
            cafe.setEmail(rawTags.get("email").toString());
        } else if (props.getContact() != null) {
            cafe.setEmail(props.getContact().getEmail());
        }

        if (rawTags.containsKey("website")) {
            cafe.setWebsite(rawTags.get("website").toString());
        } else {
            cafe.setWebsite(props.getWebsite());
        }
    }

    private void mapServicios(Cafe cafe, PropertiesDTO props, Map<String, Object> rawTags) {
        if (rawTags.containsKey("internet_access")) {
            String access = rawTags.get("internet_access").toString().toLowerCase();
            cafe.setInternetAccess(access.equals("yes") || access.equals("wlan"));
        } else if (props.getFacilities() != null) {
            cafe.setInternetAccess(props.getFacilities().getInternet_access());
        } else {
            cafe.setInternetAccess(false);
        }

        if (rawTags.containsKey("outdoor_seating")) {
            cafe.setOutdoorSeating(rawTags.get("outdoor_seating").toString().equalsIgnoreCase("yes"));
        } else if (props.getFacilities() != null) {
            cafe.setOutdoorSeating(props.getFacilities().getOutdoor_seating());
        } else {
            cafe.setOutdoorSeating(false);
        }

        if (rawTags.containsKey("delivery")) {
            cafe.setDelivery(rawTags.get("delivery").toString().equalsIgnoreCase("yes"));
        } else if (props.getFacilities() != null) {
            cafe.setDelivery(props.getFacilities().getDelivery());
        } else {
            cafe.setDelivery(false);
        }

        if (rawTags.containsKey("takeaway")) {
            cafe.setTakeaway(rawTags.get("takeaway").toString().equalsIgnoreCase("yes"));
        } else if (props.getFacilities() != null) {
            cafe.setTakeaway(props.getFacilities().getTakeaway());
        } else {
            cafe.setTakeaway(false);
        }
    }

    private void mapOperacion(Cafe cafe, PropertiesDTO props, Map<String, Object> rawTags) {

        if (props.getOsm_id() != null) {
            cafe.setOsmId(props.getOsm_id());
        } else if (rawTags.containsKey("osm_id")) {
            try {
                cafe.setOsmId(Long.parseLong(rawTags.get("osm_id").toString()));
            } catch (NumberFormatException e) {
                System.err.println("Error al parsear osm_id: " + rawTags.get("osm_id"));
            }
        }

        if (rawTags.containsKey("cuisine")) {
            cafe.setCuisine(rawTags.get("cuisine").toString());
        } else if (props.getCatering() != null) {
            cafe.setCuisine(props.getCatering().getCuisine());
        }

        if (rawTags.containsKey("opening_hours")) {
            cafe.setOpeningHours(rawTags.get("opening_hours").toString());
        } else if (props.getOpening_hours() != null) {
            cafe.setOpeningHours(props.getOpening_hours());
        }
    }
}
