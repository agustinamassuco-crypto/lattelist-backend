package com.example.LatteListBack.Services;

import com.example.LatteListBack.DTOs.AuthDTOs.AuthResponseDTO;
import com.example.LatteListBack.DTOs.UserDTOs.UsuarioFactory;
import com.example.LatteListBack.DTOs.UserDTOs.UsuarioListDTO;
import com.example.LatteListBack.DTOs.UserDTOs.UsuarioRegistroDTO;
import com.example.LatteListBack.Enums.EstadoUsuario;
import com.example.LatteListBack.Enums.TipoDeUsuario;
import com.example.LatteListBack.Models.Usuario;
import com.example.LatteListBack.Repositorys.UserRepository;
import com.example.LatteListBack.Config.JwtService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final ListaDeCafesService listaService;
    private final ReviewService reviewService;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, EmailService emailService, @Lazy ListaDeCafesService listaService, @Lazy ReviewService reviewService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.listaService = listaService;
        this.reviewService = reviewService;
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

        Usuario guardado = userRepository.save(u);

        listaService.crearListaFavoritos(guardado);
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

    public List<UsuarioListDTO> obtenerTodosLosUsuarios() {
        List<EstadoUsuario> estadosVisibles = Arrays.asList(
                EstadoUsuario.ACTIVO,
                EstadoUsuario.INACTIVO
        );

        return userRepository.findByEstadoIn(estadosVisibles).stream()
                .map(UsuarioFactory::toListDTO)
                .collect(Collectors.toList());
    }



    public Usuario getUsuarioAutenticado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
    }

    public boolean existeEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
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

    public UsuarioListDTO getUsuarioPorId(Long id) {
        Usuario usuario = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        return UsuarioFactory.toListDTO(usuario);
    }

    @Transactional
    public void cambiarEstadoUsuario(Long id, EstadoUsuario nuevoEstado) {
        Usuario u = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));

        if (u.getEstado() == nuevoEstado) return;
        EstadoUsuario estadoAnterior = u.getEstado();

        //Si eliminamos el user hacemos soft delete, eliminados de verdad sus listas
        //y pasamos sus resenias a estado eliminado, tambien liberamos el email del user
        if (nuevoEstado == EstadoUsuario.ELIMINADO) {
            String emailOriginal = u.getEmail();
            listaService.eliminarListasDeUsuario(u.getUsername());
            reviewService.eliminarReviewsPorUsuario(id);

            emailService.enviarCorreoEliminacion(emailOriginal, u.getNombre());

            String emailLiberado = u.getEmail() + "_deleted_" + System.currentTimeMillis();
            u.setEmail(emailLiberado);
        }


        //le avisamos por mail que fue inactivado por el admin
        else if (nuevoEstado == EstadoUsuario.INACTIVO) {
            emailService.enviarNotificacionSuspension(u.getEmail(), u.getNombre());

            //le mandamos mail cuando de de baja su cuenta
        } else if (nuevoEstado == EstadoUsuario.DESACTIVADO) {
            emailService.enviarNotificacionPausa(u.getEmail(), u.getNombre());
        }
        //el admin me perdono
        else if (nuevoEstado == EstadoUsuario.ACTIVO && estadoAnterior == EstadoUsuario.INACTIVO) {
            emailService.enviarNotificacionReactivacion(u.getEmail(), u.getNombre());
        }

        u.setEstado(nuevoEstado);
        userRepository.save(u);
    }






}