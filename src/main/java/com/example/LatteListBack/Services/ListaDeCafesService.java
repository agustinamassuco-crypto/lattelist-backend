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

    public ListResumenDTO crearLista(ListRequestDTO dto) {
        Usuario usuario = userService.getUsuarioAutenticado();

        ListaDeCafes lista = new ListaDeCafes();
        lista.setNombre(dto.nombre());
        lista.setUsuario(usuario);
        lista.setFecha(LocalDate.now());

        ListaDeCafes guardada = listaRepository.save(lista);
        return ListFactory.toListDTO(guardada);
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


}