package com.example.LatteListBack.Controllers;

import com.example.LatteListBack.DTOs.ListDTOs.ListRequestDTO;
import com.example.LatteListBack.DTOs.ListDTOs.ListResumenDTO;
import com.example.LatteListBack.Services.ListaDeCafesService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    @GetMapping("/publicas")
    public ResponseEntity<List<ListResumenDTO>> getListasPublicas() {
        return ResponseEntity.ok(listaService.obtenerListasPublicas());
    }

    @PreAuthorize("hasAuthority('CLIENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<ListResumenDTO> getListById(@PathVariable Long id) {
        return ResponseEntity.ok(listaService.obtenerPorId(id));
    }


    @PreAuthorize("hasAuthority('CLIENTE')")
    @PostMapping
    public ResponseEntity<ListResumenDTO> crearLista(@RequestBody ListRequestDTO dto) {
        return ResponseEntity.ok(listaService.crearLista(dto));
    }

    @PreAuthorize("hasAuthority('CLIENTE')")
    @PostMapping("/{id}/clonar")
    public ResponseEntity<ListResumenDTO> clonarLista(@PathVariable Long id) {
        return ResponseEntity.ok(listaService.clonarLista(id));
    }


    @PreAuthorize("hasAuthority('CLIENTE')")
    @PutMapping("/{id}")
    public ResponseEntity<ListResumenDTO> updateLista(@PathVariable Long id, @RequestBody ListResumenDTO dto) {
        return ResponseEntity.ok(listaService.actualizarListaCompleta(id, dto));
    }

    @PatchMapping("/{id}/visibilidad")
    public ResponseEntity<Void> cambiarVisibilidad(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        Boolean publica = body.get("publica");
        if (publica == null) return ResponseEntity.badRequest().build();
        listaService.cambiarVisibilidad(id, publica);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('CLIENTE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLista(@PathVariable Long id) {
        listaService.eliminarLista(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('CLIENTE')")
    @PostMapping("/{listaId}/cafes/{cafeId}")
    public ResponseEntity<Void> agregarCafe(@PathVariable Long listaId, @PathVariable Long cafeId) {
        listaService.agregarCafe(listaId, cafeId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('CLIENTE')")
    @DeleteMapping("/{listaId}/cafes/{cafeId}")
    public ResponseEntity<Void> quitarCafe(@PathVariable Long listaId, @PathVariable Long cafeId) {
        listaService.quitarCafe(listaId, cafeId);
        return ResponseEntity.noContent().build();
    }
}