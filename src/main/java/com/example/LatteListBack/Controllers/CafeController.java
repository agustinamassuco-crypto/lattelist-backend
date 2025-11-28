package com.example.LatteListBack.Controllers;

import com.example.LatteListBack.DTOs.CoffeDTOs.CafeListDTO;
import com.example.LatteListBack.Services.CafeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cafes")
public class CafeController {

    @Autowired
    private CafeService service;


    @GetMapping
    public ResponseEntity<List<CafeListDTO>> getAll(){
        List<CafeListDTO> cafes = service.listarCafes();
        return ResponseEntity.ok(cafes);
    }

    @PostMapping("/actualizar-cafes")
    public void actualizar() {
        service.actualizarCafesDesdeApi();
    }
}
