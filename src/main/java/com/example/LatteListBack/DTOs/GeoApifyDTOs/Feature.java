package com.example.LatteListBack.DTOs.GeoApifyDTOs;


public class Feature {

    private PropertiesDTO properties;
    private Geometry geometry;

    public PropertiesDTO getProperties() {
        return properties;
    }
    public void setProperties(PropertiesDTO propertiesDTO) {
        this.properties = propertiesDTO;
    }
    public Geometry getGeometry() {
        return geometry;
    }
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
}
