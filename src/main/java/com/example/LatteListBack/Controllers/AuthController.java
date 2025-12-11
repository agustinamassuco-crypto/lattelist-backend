package com.example.LatteListBack.Controllers;

import com.example.LatteListBack.Config.JwtService;
import com.example.LatteListBack.DTOs.AuthDTOs.AuthRequestDTO;
import com.example.LatteListBack.DTOs.AuthDTOs.AuthResponseDTO;
import com.example.LatteListBack.DTOs.UserDTOs.UsuarioFactory;
import com.example.LatteListBack.DTOs.UserDTOs.UsuarioRegistroDTO;
import com.example.LatteListBack.Models.Usuario;
import com.example.LatteListBack.Repositorys.UserRepository;
import com.example.LatteListBack.Services.UserService;
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
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody UsuarioRegistroDTO request) {
        return ResponseEntity.ok(userService.registrarUsuarioCliente(request));
    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.existeEmail(email));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request) {
       // System.out.println("INTENTO DE LOGIN RECIBIDO: " + request.email());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        Usuario user = userRepository.findByEmail(request.email()).orElseThrow();

        String jwtToken = jwtService.generateToken(user);

        AuthResponseDTO response = UsuarioFactory.toAuthResponse(user, jwtToken);
        return ResponseEntity.ok(response);
    }
}