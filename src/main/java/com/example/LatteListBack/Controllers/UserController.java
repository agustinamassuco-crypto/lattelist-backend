package com.example.LatteListBack.Controllers;

import com.example.LatteListBack.DTOs.AuthDTOs.AuthResponseDTO;
import com.example.LatteListBack.DTOs.AuthDTOs.ChangePasswordDTO;
import com.example.LatteListBack.DTOs.UserDTOs.UsuarioListDTO;
import com.example.LatteListBack.DTOs.UserDTOs.UsuarioRegistroDTO;
import com.example.LatteListBack.Enums.EstadoUsuario;
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


    /*@GetMapping("/count-admins")
    public ResponseEntity<Long> contarAdmins() {
        return ResponseEntity.ok(userService.contarAdminsActivos());
    }*/

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioListDTO> obtenerUsuarioPorId(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUsuarioPorId(id));
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> cambiarContrasena(@RequestBody ChangePasswordDTO dto) {
        userService.cambiarContrasena(dto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String estadoStr = body.get("estado");
        if (estadoStr == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            EstadoUsuario nuevoEstado = EstadoUsuario.valueOf(estadoStr);
            userService.cambiarEstadoUsuario(id, nuevoEstado);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }


}
