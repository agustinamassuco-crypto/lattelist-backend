package com.example.LatteListBack.Controllers;

import com.example.LatteListBack.DTOs.CoffeDTOs.CafeDetailDTO;
import com.example.LatteListBack.DTOs.CoffeDTOs.CafeListDTO;
import com.example.LatteListBack.Services.CafeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/cafes")
public class CafeController {

    @Autowired
    private CafeService service;

    @GetMapping
    public ResponseEntity<List<CafeListDTO>> getAll(
            @RequestParam(required = false) Boolean delivery,
            @RequestParam(required = false) Boolean takeaway,
            @RequestParam(required = false) Boolean internet_access,
            @RequestParam(required = false) Boolean outdoor_seating,
            @RequestParam(required = false) Boolean abiertoAhora,
            @RequestParam(required = false) String search) {

        List<CafeListDTO> cafes = service.listarCafes(
                delivery,
                takeaway,
                internet_access,
                outdoor_seating,
                abiertoAhora,
                search
        );

        return ResponseEntity.ok(cafes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CafeDetailDTO> getById(@PathVariable Long id) {
        CafeDetailDTO cafe = service.buscarPorId(id);
        return ResponseEntity.ok(cafe);
    }

}
