package com.example.LatteListBack.Mappers;

import com.example.LatteListBack.DTOs.ReviewDTOs.ReviewRequestDTO;
import com.example.LatteListBack.DTOs.ReviewDTOs.ReviewResponseDTO;
import com.example.LatteListBack.Enums.CostoPromedio;
import com.example.LatteListBack.Enums.Etiquetas;
import com.example.LatteListBack.Enums.TipoReaccion;
import com.example.LatteListBack.Models.Cafe;
import com.example.LatteListBack.Models.Review;
import com.example.LatteListBack.Models.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReviewMapper {

        public Review toEntity(ReviewRequestDTO dto, Usuario user, Cafe cafe) {

            Review review = new Review();

            review.setUsuario(user);
            review.setCafe(cafe);
            review.setPuntuacion(dto.getPuntuacion());
            review.setComentario(dto.getComentario());
            review.setCostoPromedio(mapCosto(dto.getCostoPromedio()));
            review.setEtiquetas(mapEtiquetas(dto.getEtiquetas()));
            review.setFotos(dto.getFotos());

            return review;
        }

        public ReviewResponseDTO toDTO(Review review,
                                       Long likes,
                                       Long dislikes,
                                       TipoReaccion reaccionUsuario) {

            ReviewResponseDTO dto = new ReviewResponseDTO();

            dto.setId(review.getId());
            dto.setPuntuacion(review.getPuntuacion());
            dto.setComentario(review.getComentario());
            dto.setFecha(review.getFecha().toString());

            dto.setCostoPromedio(review.getCostoPromedio());
            dto.setEtiquetas(review.getEtiquetas());
            dto.setFotos(review.getFotos());

            dto.setLikes(likes != null ? likes.intValue() : 0);
            dto.setDislikes(dislikes != null ? dislikes.intValue() : 0);
            dto.setReaccionActualUsuario(reaccionUsuario);

            // 🔥 AUTOR
            Usuario user = review.getUsuario();
            dto.setUserId(user.getId());
            dto.setUserNombre(user.getNombre());
            dto.setUserApellido(user.getApellido());
            dto.setUserFotoPerfil(user.getFotoPerfil());

            dto.setCafeId(review.getCafe().getId());
            dto.setEstado(review.getEstado());

            return dto;
        }

        public CostoPromedio mapCosto(String s) {
            if (s == null || s.isBlank()) return null;

            return switch (s.toUpperCase()) {
                case "BARATO", "$" -> CostoPromedio.BARATO;
                case "MEDIO", "$$" -> CostoPromedio.MEDIO;
                case "CARO", "$$$" -> CostoPromedio.CARO;
                default -> throw new IllegalArgumentException("Costo inválido: " + s);
            };
        }

        public List<Etiquetas> mapEtiquetas(List<String> etiquetas) {
            if (etiquetas == null || etiquetas.isEmpty()) return List.of();

            return etiquetas.stream()
                    .map(e -> Etiquetas.valueOf(e.toUpperCase()))
                    .collect(Collectors.toList());
        }
    }


/*
    public Review toEntity(ReviewRequestDTO dto, Usuario user, Cafe cafe) {

        Review review = new Review();

        review.setUsuario(user);
        review.setCafe(cafe);

        review.setPuntuacion(dto.getPuntuacion());
        review.setComentario(dto.getComentario());

        review.setCostoPromedio(mapCosto(dto.getCostoPromedio()));
        review.setEtiquetas(mapEtiquetas(dto.getEtiquetas()));
        review.setFotos(dto.getFotos());

        return review;
    }

    public ReviewResponseDTO toDTO(Review review,
                                   Long likes,
                                   Long dislikes,
                                   TipoReaccion reaccionUsuario) {

        ReviewResponseDTO dto = new ReviewResponseDTO();

        dto.setId(review.getId());
        dto.setPuntuacion(review.getPuntuacion());
        dto.setComentario(review.getComentario());
        dto.setFecha(review.getFecha().toString());

        dto.setCostoPromedio(review.getCostoPromedio());
        dto.setEtiquetas(review.getEtiquetas());
        dto.setFotos(review.getFotos());

        dto.setLikes(likes.intValue());
        dto.setDislikes(dislikes.intValue());
        dto.setReaccionActualUsuario(reaccionUsuario);

        dto.setUserId(review.getUsuario().getId());
        dto.setCafeId(review.getCafe().getId());

        dto.setEstado(review.getEstado());

        return dto;
    }

    public CostoPromedio mapCosto(String s) {
        if (s == null || s.isBlank()) return null;

        return switch (s.toUpperCase()) {
            case "BARATO", "$" -> CostoPromedio.BARATO;
            case "MEDIO", "$$" -> CostoPromedio.MEDIO;
            case "CARO", "$$$" -> CostoPromedio.CARO;
            default -> throw new IllegalArgumentException("Costo inválido: " + s);
        };
    }



    public List<Etiquetas> mapEtiquetas(List<String> etiquetas) {
        if (etiquetas == null) return null;
        return etiquetas.stream()
                .map(e -> Etiquetas.valueOf(e))
                .collect(Collectors.toList());
    }*/

