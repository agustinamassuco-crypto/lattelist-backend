package com.example.LatteListBack.Models;

import com.example.LatteListBack.Enums.CostoPromedio;
import com.example.LatteListBack.Enums.EstadoReview;
import com.example.LatteListBack.Enums.Etiquetas;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;



@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer puntuacion;

    private String comentario;

    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    private CostoPromedio costoPromedio;

    @ElementCollection(targetClass = Etiquetas.class)
    @CollectionTable(name = "review_etiquetas", joinColumns = @JoinColumn(name = "review_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "etiqueta")
    private List<Etiquetas> etiquetas = new ArrayList<>();

     @ElementCollection
    @CollectionTable(name = "review_fotos", joinColumns = @JoinColumn(name = "review_id"))
    @Column(name = "foto", columnDefinition = "LONGTEXT")
    private List<String> fotos = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoReview estado = EstadoReview.ACTIVA;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id", nullable = false)
    private Cafe cafe;

    public Review() {
    }

    @PrePersist
    protected void onCreate() {
        this.fecha = LocalDate.now();
    }

    public CostoPromedio getCostoPromedio() {
        return costoPromedio;
    }

    public void setCostoPromedio(CostoPromedio costoPromedio) {
        this.costoPromedio = costoPromedio;
    }

    public Long getId() {
        return id;
    }


    public Integer getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Integer puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public List<Etiquetas> getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(List<Etiquetas> etiquetas) {
        this.etiquetas = etiquetas;
    }

    public EstadoReview getEstado() {
        return estado;
    }

    public void setEstado(EstadoReview estado) {
        this.estado = estado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Cafe getCafe() {
        return cafe;
    }

    public void setCafe(Cafe cafe) {
        this.cafe = cafe;
    }
}
