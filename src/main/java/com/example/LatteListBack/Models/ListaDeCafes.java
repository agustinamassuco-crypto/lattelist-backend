package com.example.LatteListBack.Models;


import jakarta.persistence.*;

@Entity
@Table(name = "listas")
public class ListaDeCafes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
