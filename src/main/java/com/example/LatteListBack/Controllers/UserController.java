package com.example.LatteListBack.Controllers;

import com.example.LatteListBack.DTOs.AuthDTOs.AuthResponseDTO;
import com.example.LatteListBack.DTOs.UserDTOs.UsuarioListDTO;
import com.example.LatteListBack.DTOs.UserDTOs.UsuarioRegistroDTO;
import com.example.LatteListBack.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/crear-admin")
    public ResponseEntity<AuthResponseDTO> crearAdmin(@RequestBody UsuarioRegistroDTO request) {
        return ResponseEntity.ok(userService.registrarUsuarioAdmin(request));
    }

    @PutMapping("/me")
    public ResponseEntity<AuthResponseDTO> editarPerfil(@RequestBody UsuarioRegistroDTO request) {
        return ResponseEntity.ok(userService.actualizarMiPerfil(request));
    }

    @GetMapping("/listado")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UsuarioListDTO>> listarUsuariosParaAdmin() {
        return ResponseEntity.ok(userService.obtenerTodosLosUsuarios());
    }
/*
   @GetMapping("/perfil")
    public ResponseEntity<UsuarioCompletoDto> verMiPerfil() {
        return ResponseEntity.ok(userService.obtenerMiPerfil());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}/perfil")
    public ResponseEntity<UsuarioCompletoDto> verPerfilDeOtro(@PathVariable Long id) {
        return ResponseEntity.ok(userService.obtenerPerfilPorId(id));
    }*/

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        userService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count-admins")
    public ResponseEntity<Long> contarAdmins() {
        return ResponseEntity.ok(userService.contarAdminsActivos());
    }

  /*  @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String nuevoEstado = body.get("estado"); // Espera "INACTIVO" o "ACTIVO"
        userService.cambiarEstadoUsuario(id, nuevoEstado);
        return ResponseEntity.ok().build();
    }*/

}
