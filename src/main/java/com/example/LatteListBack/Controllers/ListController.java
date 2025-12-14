package com.example.LatteListBack.Controllers;

import com.example.LatteListBack.DTOs.ListDTOs.ListRequestDTO;
import com.example.LatteListBack.DTOs.ListDTOs.ListResumenDTO;
import com.example.LatteListBack.Services.ListaDeCafesService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/listas")
public class ListController {

    private final ListaDeCafesService listaService;

    public ListController(ListaDeCafesService listaService) {
        this.listaService = listaService;
    }

    @PreAuthorize("hasAuthority('CLIENTE')")
    @GetMapping("/mis-listas")
    public ResponseEntity<List<ListResumenDTO>> getMisListas() {
        return ResponseEntity.ok(listaService.obtenerMisListas());
    }

    @PreAuthorize("hasAuthority('CLIENTE')")
    @PostMapping
    public ResponseEntity<ListResumenDTO> crearLista(@RequestBody ListRequestDTO dto) {
        return ResponseEntity.ok(listaService.crearLista(dto));
    }

    @PreAuthorize("hasAuthority('CLIENTE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLista(@PathVariable Long id) {
        listaService.eliminarLista(id);
        return ResponseEntity.noContent().build();
    }
}
