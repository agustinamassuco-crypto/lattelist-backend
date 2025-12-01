package com.example.LatteListBack.Enums;


//no olvidar de hacer el endpoint para etiquetas
public enum Etiquetas {

    BRUNCH("Brunch"),
    DESAYUNOS("Desayunos"),
    CAFETERIA_ESPECIALIDAD("Cafetería de Especialidad"),
    PANADERIA_ARTESANAL("Panadería Artesanal"),
    PASTELERIA("Pastelería"),
    COMIDA_VEGANA("Comida Vegana"),
    COMIDA_SALUDABLE("Comida Saludable"),
    SIN_TACC("Sin TACC"),
    OPCIONES_SIN_LACTOSA("Opciones Sin Lactosa"),
    SANDWICHES_GOURMET("Sandwiches Gourmet"),
    POSTRES_CASEROS("Postres Caseros"),
    JUGOS_NATURALES("Jugos Naturales"),
    SMOOTHIES("Smoothies"),
    TEMATICO("Temático"),
    MINIMALISTA("Minimalista"),
    VINTAGE("Vintage"),
    PET_FRIENDLY("Pet Friendly"),
    LIBROS("Libros"),
    ARTE("Arte"),
    MUSICA_EN_VIVO("Música en Vivo"),
    ESTILO_COWORKING("Estilo Coworking"),
    FRENTE_AL_MAR("Frente al Mar"),
    ZONA_CENTRICA("Zona Céntrica"),
    ENCHUFES_DISPONIBLES("Enchufes Disponibles"),
    TERRAZA("Terraza"),
    PATIO("Patio"),
    CALEFACCION("Calefacción"),
    AIRE_ACONDICIONADO("Aire Acondicionado"),
    ESTUDIANTES("Estudiantes"),
    GRUPOS_GRANDES("Grupos Grandes"),
    FAMILIAR("Familiar"),
    PAREJAS("Parejas");

    private final String label;

    Etiquetas(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

