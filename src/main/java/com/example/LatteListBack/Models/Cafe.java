package com.example.LatteListBack.Models;


import jakarta.persistence.*;

@Entity
@Table(name = "cafes")
public class Cafe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
