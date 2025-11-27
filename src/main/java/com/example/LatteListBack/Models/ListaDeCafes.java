package com.example.LatteListBack.Models;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "listas")
public class ListaDeCafes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ElementCollection
    @CollectionTable(name = "lista_cafes", joinColumns = @JoinColumn(name = "lista_id"))
    @Column(name = "cafe_id")
    private List<Long> idCafes = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "lista_cafes_visitados", joinColumns = @JoinColumn(name = "lista_id"))
    @Column(name = "cafe_id")
    private List<Long> idCafesVisitados = new ArrayList<>();

    private LocalDate fecha ;

    @PrePersist
    protected void onCreate() {
        this.fecha = LocalDate.now();
    }


}
