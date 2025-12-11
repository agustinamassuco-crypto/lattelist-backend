package com.example.LatteListBack.DTOs.UserDTOs;

import com.example.LatteListBack.DTOs.AuthDTOs.AuthResponseDTO;
import com.example.LatteListBack.Enums.TipoDeUsuario;
import com.example.LatteListBack.Models.Usuario;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UsuarioFactory {

    private UsuarioFactory() {
    }

    // login y registrarse
    public static AuthResponseDTO toAuthResponse(Usuario u, String token) {
        return new AuthResponseDTO(
                token,
                u.getId(),
                u.getNombre(),
                u.getApellido(),
                u.getUsername(),
                u.getTipoDeUsuario(),
                u.getFotoPerfil(),
                u.getEstado().name()
        );
    }

    //para ver el perfill
    public static UsuarioDetailDTO toDetailDTO(Usuario u) {
        return new UsuarioDetailDTO(
                u.getId(),
                u.getNombre(),
                u.getApellido(),
                u.getEmail(),
                u.getTipoDeUsuario(),
                u.getFotoPerfil(),
                u.getEstado().name()
        );
    }

    //lista de users para admin
    public static UsuarioListDTO toListDTO(Usuario u) {
        return new UsuarioListDTO(
                u.getId(),
                u.getNombre(),
                u.getApellido(),
                u.getEmail(),
                u.getTipoDeUsuario()
        );
    }

    public static Usuario createFromRegisterDTO(UsuarioRegistroDTO dto, PasswordEncoder encoder, TipoDeUsuario rol) {
        Usuario u = new Usuario();
        u.setNombre(dto.nombre());
        u.setApellido(dto.apellido());
        u.setEmail(dto.email());
        u.setPassword(encoder.encode(dto.password()));
        u.setTipoDeUsuario(rol);
        u.setFotoPerfil(dto.fotoPerfil());
        return u;
    }
}
