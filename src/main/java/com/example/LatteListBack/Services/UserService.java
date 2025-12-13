package com.example.LatteListBack.Services;

import com.example.LatteListBack.DTOs.AuthDTOs.AuthResponseDTO;
import com.example.LatteListBack.DTOs.UserDTOs.UsuarioFactory;
import com.example.LatteListBack.DTOs.UserDTOs.UsuarioRegistroDTO;
import com.example.LatteListBack.Enums.EstadoUsuario;
import com.example.LatteListBack.Enums.TipoDeUsuario;
import com.example.LatteListBack.Models.ListaDeCafes;
import com.example.LatteListBack.Models.Usuario;
import com.example.LatteListBack.Repositorys.UserRepository;
import com.example.LatteListBack.Config.JwtService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    public AuthResponseDTO registrarUsuarioCliente(UsuarioRegistroDTO request) {
        return guardarUsuario(request, TipoDeUsuario.CLIENTE);
    }

    public AuthResponseDTO registrarUsuarioAdmin(UsuarioRegistroDTO request) {
        return guardarUsuario(request, TipoDeUsuario.ADMIN);
    }



    private AuthResponseDTO guardarUsuario(UsuarioRegistroDTO req, TipoDeUsuario rol) {
        if (userRepository.findByEmail(req.email()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        Usuario u = UsuarioFactory.createFromRegisterDTO(req, passwordEncoder, rol);

        //pasarlo despues haciendo el metodo en list service
        ListaDeCafes favoritos = new ListaDeCafes();
        favoritos.setNombre("Favoritos");
        favoritos.setUsuario(u);
        u.getListasDeCafes().add(favoritos);

        Usuario guardado = userRepository.save(u);
        emailService.enviarCorreoBienvenida(guardado.getEmail(), guardado.getNombre());
        String token = jwtService.generateToken(guardado);

        return UsuarioFactory.toAuthResponse(guardado, token);
    }



    public AuthResponseDTO actualizarMiPerfil(UsuarioRegistroDTO req) {
        Usuario u = getUsuarioAutenticado();

        u.setNombre(req.nombre());
        u.setApellido(req.apellido());

        if (!req.email().equalsIgnoreCase(u.getEmail())) {
            if (userRepository.findByEmail(req.email()).isPresent()) {
                throw new IllegalArgumentException("El email ya está registrado por otro usuario.");
            }
            u.setEmail(req.email());
        }

        if (req.fotoPerfil() != null) {
            u.setFotoPerfil(req.fotoPerfil());
        }

        Usuario actualizado = userRepository.save(u);

        String token = jwtService.generateToken(actualizado);

        return UsuarioFactory.toAuthResponse(actualizado, token);
    }

  /*  public UsuarioCompletoDTO obtenerMiPerfil() {
        Usuario u = getUsuarioAutenticado();
        return UsuarioFactory.toCompletoDTO(u);
    }

    public UsuarioCompletoDTO obtenerPerfilPorId(Long id) {
        Usuario u = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));
        return UsuarioFactory.toCompletoDTO(u);
    }*/



    public Usuario getUsuarioAutenticado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
    }

    public boolean existeEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public void eliminarUsuario(Long id) {
        if (!userRepository.existsById(id)) throw new EntityNotFoundException("Usuario no existe");
        userRepository.deleteById(id);
    }

    public Long contarAdminsActivos() {
        return userRepository.countByTipoDeUsuarioAndEstado(
                TipoDeUsuario.ADMIN,
                EstadoUsuario.ACTIVO
        );
    }

    public void solicitarRecuperacion(String email) {
        userRepository.findByEmail(email).ifPresent(u -> {
            String token = jwtService.generateToken(u);

            emailService.enviarCorreoRecuperacion(email, token);
        });
    }

    public void cambiarClaveConToken(String token, String nuevaPassword) {
        String email = jwtService.extractUsername(token);
        Usuario u = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        if (!jwtService.isTokenValid(token, u)) {
            throw new IllegalArgumentException("Token inválido o expirado");
        }

        u.setPassword(passwordEncoder.encode(nuevaPassword));
        userRepository.save(u);
    }

    /* a esto le falta
    public void cambiarEstadoUsuario(Long id, String nuevoEstado) {
        Usuario u = userRepository.findById(id).orElseThrow();
        try {
            u.setEstado(EstadoUsuario.valueOf(nuevoEstado));
            userRepository.save(u);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado inválido: " + nuevoEstado);
        }
    }*/
}