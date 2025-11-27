package com.example.LatteListBack.Models;

import com.example.LatteListBack.Enums.CostoPromedio;
import com.example.LatteListBack.Enums.EstadoReview;
import com.example.LatteListBack.Enums.Etiquetas;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private Integer puntuacion;

    @Column(nullable = false)
    private String comentario;

    // Fecha autogenerada
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    private CostoPromedio costoPromedio;

    @ElementCollection(targetClass = Etiquetas.class)
    @CollectionTable(name = "review_etiquetas", joinColumns = @JoinColumn(name = "review_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "etiqueta")
    private List<Etiquetas> etiquetas = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoReview estado = EstadoReview.ACTIVA;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonBackReference
    private Usuario usuario;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id", nullable = false)
    @JsonBackReference
    private Cafe cafe;

    @PrePersist
    protected void onCreate() {
        this.fecha = LocalDate.now();
    }
}
