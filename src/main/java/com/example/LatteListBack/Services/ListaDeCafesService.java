package com.example.LatteListBack.Services;

import com.example.LatteListBack.DTOs.ListDTOs.ListFactory;
import com.example.LatteListBack.DTOs.ListDTOs.ListRequestDTO;
import com.example.LatteListBack.DTOs.ListDTOs.ListResumenDTO;
import com.example.LatteListBack.DTOs.UserDTOs.UsuarioFactory;
import com.example.LatteListBack.Models.ListaDeCafes;
import com.example.LatteListBack.Models.Usuario;
import com.example.LatteListBack.Repositorys.ListaDeCafesRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListaDeCafesService {

    private final ListaDeCafesRepository listaRepository;
    private final UserService userService;

    public ListaDeCafesService(ListaDeCafesRepository listaRepository, UserService userService) {
        this.listaRepository = listaRepository;
        this.userService = userService;
    }

    public List<ListResumenDTO> obtenerMisListas() {
        Usuario usuario = userService.getUsuarioAutenticado();
        List<ListaDeCafes> listas = listaRepository.findByUsuario_Email(usuario.getUsername());

        return listas.stream()
                .map(ListFactory::toListDTO)
                .collect(Collectors.toList());
    }


    public ListResumenDTO obtenerPorId(Long id) {
        Usuario usuario = userService.getUsuarioAutenticado();
        ListaDeCafes lista = listaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lista no encontrada"));

        if (!lista.getUsuario().getId().equals(usuario.getId()) && !lista.getPublica()) {
            throw new AccessDeniedException("No tienes permiso para ver esta lista.");
        }

        return ListFactory.toListDTO(lista);
    }


    public ListResumenDTO crearLista(ListRequestDTO dto) {
        Usuario usuario = userService.getUsuarioAutenticado();
        ListaDeCafes lista = new ListaDeCafes();
        lista.setNombre(dto.nombre());
        lista.setUsuario(usuario);
        lista.setPublica(false);
        return ListFactory.toListDTO(listaRepository.save(lista));
    }

    public void eliminarLista(Long idLista) {
        Usuario usuario = userService.getUsuarioAutenticado();
        ListaDeCafes lista = listaRepository.findById(idLista)
                .orElseThrow(() -> new EntityNotFoundException("Lista no encontrada"));

        if (!lista.getUsuario().getId().equals(usuario.getId())) {
            throw new AccessDeniedException("No tienes permiso para eliminar esta lista");
        }

        listaRepository.delete(lista);
    }



    public List<ListResumenDTO> obtenerListasPublicas() {
        return listaRepository.findByPublicaTrue().stream()
                .map(ListFactory::toListDTO)
                .collect(Collectors.toList());
    }


    public void crearListaFavoritos(Usuario usuario) {
        ListaDeCafes favoritos = new ListaDeCafes();
        favoritos.setNombre("Favoritos");
        favoritos.setUsuario(usuario);

        listaRepository.save(favoritos);
    }

    @Transactional
    public void eliminarListasDeUsuario(String email) {
        List<ListaDeCafes> listas = listaRepository.findByUsuario_Email(email);
        listaRepository.deleteAll(listas);
    }


    @Transactional
    public void agregarCafe(Long listaId, Long cafeId) {
        Usuario usuario = userService.getUsuarioAutenticado();

        ListaDeCafes lista = listaRepository.findById(listaId)
                .orElseThrow(() -> new EntityNotFoundException("Lista no encontrada"));

        if (!lista.getUsuario().getId().equals(usuario.getId())) {
            throw new AccessDeniedException("No puedes modificar esta lista");
        }

        if (lista.getIdCafes().add(cafeId)) {
            listaRepository.save(lista);
        }
    }

    @Transactional
    public void quitarCafe(Long listaId, Long cafeId) {
        Usuario usuario = userService.getUsuarioAutenticado();

        ListaDeCafes lista = listaRepository.findById(listaId)
                .orElseThrow(() -> new EntityNotFoundException("Lista no encontrada"));

        if (!lista.getUsuario().getId().equals(usuario.getId())) {
            throw new AccessDeniedException("No puedes modificar esta lista");
        }

        if (lista.getIdCafes().remove(cafeId)) {
            listaRepository.save(lista);
        }
    }

    @Transactional
    public ListResumenDTO clonarLista(Long listaIdOriginal) {
        Usuario usuario = userService.getUsuarioAutenticado();

        ListaDeCafes original = listaRepository.findById(listaIdOriginal)
                .orElseThrow(() -> new EntityNotFoundException("Lista original no encontrada"));

        if (!original.getPublica() && !original.getUsuario().getId().equals(usuario.getId())) {
            throw new AccessDeniedException("No puedes clonar una lista privada ajena.");
        }

        ListaDeCafes copia = new ListaDeCafes();
        copia.setUsuario(usuario);
        copia.setNombre(original.getNombre() + " (Copia)");
        copia.setFecha(LocalDate.now());
        copia.setPublica(false);

        copia.setIdCafes(new HashSet<>(original.getIdCafes()));
        copia.setIdCafesVisitados(new HashSet<>());

        return ListFactory.toListDTO(listaRepository.save(copia));
    }

    @Transactional
    public ListResumenDTO actualizarListaCompleta(Long id, ListResumenDTO dto) {
        Usuario usuario = userService.getUsuarioAutenticado();
        ListaDeCafes lista = listaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lista no encontrada"));

        if (!lista.getUsuario().getId().equals(usuario.getId())) {
            throw new AccessDeniedException("No puedes editar una lista que no es tuya.");
        }
        lista.setNombre(dto.nombre());

        if (dto.idCafes() != null) {
            lista.setIdCafes(new HashSet<>(dto.idCafes()));
        }
        if (dto.idCafesVisitados() != null) {
            lista.setIdCafesVisitados(new HashSet<>(dto.idCafesVisitados()));
        }
        if (dto.publica() != null) lista.setPublica(dto.publica());

        return ListFactory.toListDTO(listaRepository.save(lista));
    }



    public void cambiarVisibilidad(Long listaId, Boolean publica) {
        Usuario u = userService.getUsuarioAutenticado();
        ListaDeCafes l = listaRepository.findById(listaId).orElseThrow();
        if(!l.getUsuario().getId().equals(u.getId())) throw new AccessDeniedException("Error");
        l.setPublica(publica);
        listaRepository.save(l);
    }

}