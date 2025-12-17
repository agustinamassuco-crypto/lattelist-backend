package com.example.LatteListBack.DTOs.ReviewDTOs;

import com.example.LatteListBack.Enums.CostoPromedio;
import com.example.LatteListBack.Enums.EstadoReview;
import com.example.LatteListBack.Enums.Etiquetas;
import com.example.LatteListBack.Enums.TipoReaccion;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class ReviewResponseDTO {

    private Long id;
    private int puntuacion;
    private String comentario;
    private String fecha;

    private CostoPromedio costoPromedio;
    private List<Etiquetas> etiquetas;
    private List<String> fotos;

    private int likes;
    private int dislikes;

    @JsonInclude(JsonInclude.Include.ALWAYS)
    private TipoReaccion reaccionActualUsuario;

    private Long userId;
    private String userNombre;
    private String userApellido;
    private String userFotoPerfil;


    private Long cafeId;

    private EstadoReview estado;

    public String getUserNombre() {
        return userNombre;
    }

    public void setUserNombre(String userNombre) {
        this.userNombre = userNombre;
    }

    public String getUserApellido() {
        return userApellido;
    }

    public void setUserApellido(String userApellido) {
        this.userApellido = userApellido;
    }

    public String getUserFotoPerfil() {
        return userFotoPerfil;
    }

    public void setUserFotoPerfil(String userFotoPerfil) {
        this.userFotoPerfil = userFotoPerfil;
    }

    public EstadoReview getEstado() {
        return estado;
    }

    public void setEstado(EstadoReview estado) {
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public CostoPromedio getCostoPromedio() {
        return costoPromedio;
    }

    public void setCostoPromedio(CostoPromedio costoPromedio) {
        this.costoPromedio = costoPromedio;
    }

    public List<Etiquetas> getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(List<Etiquetas> etiquetas) {
        this.etiquetas = etiquetas;
    }

    public List<String> getFotos() {
        return fotos;
    }

    public void setFotos(List<String> fotos) {
        this.fotos = fotos;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public TipoReaccion getReaccionActualUsuario() {
        return reaccionActualUsuario;
    }

    public void setReaccionActualUsuario(TipoReaccion reaccionActualUsuario) {
        this.reaccionActualUsuario = reaccionActualUsuario;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCafeId() {
        return cafeId;
    }

    public void setCafeId(Long cafeId) {
        this.cafeId = cafeId;
    }
}
