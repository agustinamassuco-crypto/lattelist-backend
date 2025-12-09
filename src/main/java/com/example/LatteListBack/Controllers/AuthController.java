package com.example.LatteListBack.Controllers;

import com.example.LatteListBack.Config.JwtService;
import com.example.LatteListBack.DTOs.AuthDTOs.AuthRequestDTO;
import com.example.LatteListBack.DTOs.AuthDTOs.AuthResponseDTO;
import com.example.LatteListBack.Models.Usuario;
import com.example.LatteListBack.Repositorys.UserRepository; // OJO CON EL PAQUETE
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request) {
       // System.out.println("INTENTO DE LOGIN RECIBIDO: " + request.email());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        Usuario user = userRepository.findByEmail(request.email()).orElseThrow();

        String jwtToken = jwtService.generateToken(user);

        AuthResponseDTO response = new AuthResponseDTO(
                jwtToken,
                user.getId(),
                user.getNombre(),
                user.getApellido(),
                user.getUsername(),
                user.getTipoDeUsuario(),
                user.getFotoPerfil(),
                user.getEstado().name()
        );

        return ResponseEntity.ok(response);
    }
}