package com.example.LatteListBack.DTOs.ReviewDTOs;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

//El id se genera solo, la fecha se genera en el modelo, el estado //por defecto es ACTIVO en el modelo
public class ReviewRequestDTO {

    @NotNull(message = "El ID del café es obligatorio")
    private Long cafeId;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long userId;

    @NotNull(message = "La puntuación es obligatoria")
    @Min(value = 1, message = "Mínimo 1 estrella")
    @Max(value = 5, message = "Máximo 5 estrellas")
    private Integer puntuacion;

    @Size(max = 300, message = "Máximo 300 caracteres")
    private String comentario;

    private String costoPromedio;

    @Size(max = 3, message = "Máximo 3 etiquetas")
    private List<String> etiquetas;
    @Size(max = 6, message = "Máximo 6 fotos")
    private List<String> fotos; // Base64 o URLs

    public ReviewRequestDTO() {
    }

    public @NotNull(message = "El ID del café es obligatorio") Long getCafeId() {
        return cafeId;
    }


    public @NotNull(message = "El ID del usuario es obligatorio") Long getUserId() {
        return userId;
    }



    public @NotNull(message = "La puntuación es obligatoria") @Min(value = 1, message = "Mínimo 1 estrella") @Max(value = 5, message = "Máximo 5 estrellas") Integer getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(@NotNull(message = "La puntuación es obligatoria") @Min(value = 1, message = "Mínimo 1 estrella") @Max(value = 5, message = "Máximo 5 estrellas") Integer puntuacion) {
        this.puntuacion = puntuacion;
    }

    public @Size(max = 300, message = "Máximo 300 caracteres") String getComentario() {
        return comentario;
    }

    public void setComentario(@Size(max = 300, message = "Máximo 300 caracteres") String comentario) {
        this.comentario = comentario;
    }

    public String getCostoPromedio() {
        return costoPromedio;
    }

    public void setCostoPromedio(String costoPromedio) {
        this.costoPromedio = costoPromedio;
    }

    public @Size(max = 3, message = "Máximo 3 etiquetas") List<String> getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(@Size(max = 3, message = "Máximo 3 etiquetas") List<String> etiquetas) {
        this.etiquetas = etiquetas;
    }

    public @Size(max = 6, message = "Máximo 6 fotos") List<String> getFotos() {
        return fotos;
    }

    public void setFotos(@Size(max = 6, message = "Máximo 6 fotos") List<String> fotos) {
        this.fotos = fotos;
    }
}