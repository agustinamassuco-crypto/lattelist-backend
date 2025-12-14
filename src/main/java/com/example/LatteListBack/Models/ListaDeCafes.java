package com.example.LatteListBack.Models;


import jakarta.persistence.*;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


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
    private List<Long> idCafes = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "lista_cafes_visitados", joinColumns = @JoinColumn(name = "lista_id"))
    @Column(name = "cafe_id")
    private List<Long> idCafesVisitados = new ArrayList<>();

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

    public List<Long> getIdCafes() {
        return idCafes;
    }

    public void setIdCafes(List<Long> idCafes) {
        this.idCafes = idCafes;
    }

    public List<Long> getIdCafesVisitados() {
        return idCafesVisitados;
    }

    public void setIdCafesVisitados(List<Long> idCafesVisitados) {
        this.idCafesVisitados = idCafesVisitados;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
