package com.example.LatteListBack.Repositorys;

import com.example.LatteListBack.Enums.TipoDeUsuario;
import com.example.LatteListBack.Models.Cafe;
import com.example.LatteListBack.Models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByTipoDeUsuario(TipoDeUsuario tipoDeUsuario);;
    List<Usuario> findByNombre(String nombre);
    List<Usuario> findByApellido(String apellido);
    List<Usuario> findByNombreAndApellido(String nombre, String apellido);
}