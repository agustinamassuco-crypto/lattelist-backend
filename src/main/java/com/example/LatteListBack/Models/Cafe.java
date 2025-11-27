package com.example.LatteListBack.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "cafes")
public class Cafe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    private Integer osmId;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String direccion;

    private Double latitud;
    private Double longitud;

    private String telefono;
    private String email;
    private String website;

    private String openingHours;
    private Boolean takeaway;
    private Boolean delivery;
    private Boolean internetAccess;
    private Boolean outdoorSeating;

    private Boolean activo = true;

    @OneToMany(mappedBy = "cafe", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Review> resenas = new ArrayList<>();

    public Cafe() {}
}
