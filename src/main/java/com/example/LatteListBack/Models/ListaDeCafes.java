package com.example.LatteListBack.Models;


import jakarta.persistence.*;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "listas")
public class ListaDeCafes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ElementCollection
    @CollectionTable(name = "lista_cafes", joinColumns = @JoinColumn(name = "lista_id"))
    @Column(name = "cafe_id")
    private Set<Long> idCafes = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "lista_cafes_visitados", joinColumns = @JoinColumn(name = "lista_id"))
    @Column(name = "cafe_id")
    private Set<Long> idCafesVisitados = new HashSet<>();


    private LocalDate fecha ;

    public ListaDeCafes() {
    }

    @PrePersist
    protected void onCreate() {
        this.fecha = LocalDate.now();
    }


    public Long getId() {
        return id;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Long> getIdCafesVisitados() {
        return idCafesVisitados;
    }

    public void setIdCafesVisitados(Set<Long> idCafesVisitados) {
        this.idCafesVisitados = idCafesVisitados;
    }

    public Set<Long> getIdCafes() {
        return idCafes;
    }

    public void setIdCafes(Set<Long> idCafes) {
        this.idCafes = idCafes;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
