package com.example.LatteListBack.Services;

import com.example.LatteListBack.DTOs.ReviewDTOs.ReviewRequestDTO;
import com.example.LatteListBack.DTOs.ReviewDTOs.ReviewResponseDTO;
import com.example.LatteListBack.Enums.CostoPromedio;
import com.example.LatteListBack.Enums.EstadoReview;
import com.example.LatteListBack.Enums.TipoReaccion;
import com.example.LatteListBack.Mappers.ReviewMapper;
import com.example.LatteListBack.Models.Cafe;
import com.example.LatteListBack.Models.LikeReview;
import com.example.LatteListBack.Models.Review;
import com.example.LatteListBack.Models.Usuario;
import com.example.LatteListBack.Repositorys.CafeRepository;
import com.example.LatteListBack.Repositorys.LikeReviewRepository;
import com.example.LatteListBack.Repositorys.ReviewRepository;
import com.example.LatteListBack.Repositorys.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository usuarioRepository;
    private final CafeRepository cafeRepository;
    private final LikeReviewRepository reaccionRepository;
    private final ReviewMapper reviewMapper;

    public ReviewService(ReviewRepository reviewRepository,
                         UserRepository usuarioRepository,
                         CafeRepository cafeRepository,
                         LikeReviewRepository reaccionRepository,
                         ReviewMapper reviewMapper) {

        this.reviewRepository = reviewRepository;
        this.usuarioRepository = usuarioRepository;
        this.cafeRepository = cafeRepository;
        this.reaccionRepository = reaccionRepository;
        this.reviewMapper = reviewMapper;
    }


    public ReviewResponseDTO crearReview(ReviewRequestDTO request) {

        Usuario user = usuarioRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Cafe cafe = cafeRepository.findById(request.getCafeId())
                .orElseThrow(() -> new RuntimeException("Café no encontrado"));

        Review review = reviewMapper.toEntity(request, user, cafe);

        reviewRepository.save(review);

        return reviewMapper.toDTO(review, 0L, 0L, null);
    }


    public ReviewResponseDTO editarReview(Long reviewId, ReviewRequestDTO request) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review no encontrada"));

        review.setComentario(request.getComentario());
        review.setPuntuacion(request.getPuntuacion());
        if (request.getCostoPromedio() != null && !request.getCostoPromedio().isBlank()) {
            review.setCostoPromedio(
                    CostoPromedio.valueOf(request.getCostoPromedio())
            );
        } else {
            review.setCostoPromedio(null);
        }

        if (request.getEtiquetas() != null) {
            review.setEtiquetas(reviewMapper.mapEtiquetas(request.getEtiquetas()));
        } else {
            review.setEtiquetas(List.of());
        }
        review.setFotos(request.getFotos());

        reviewRepository.save(review);
        System.out.println("REVIEW USER: " + review.getUsuario().getEmail());


        long likes = reaccionRepository.countByReviewIdAndTipo(reviewId, TipoReaccion.LIKE);
        long dislikes = reaccionRepository.countByReviewIdAndTipo(reviewId, TipoReaccion.DISLIKE);

        return reviewMapper.toDTO(review, likes, dislikes, null);
    }


    public void eliminar(Long reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review no encontrada"));

        review.setEstado(EstadoReview.ELIMINADA);
        reviewRepository.save(review);
    }

    public void desactivar(Long reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review no encontrada"));

        review.setEstado(EstadoReview.INACTIVA);
        reviewRepository.save(review);
    }

    public void activar(Long reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review no encontrada"));

        review.setEstado(EstadoReview.ACTIVA);
        reviewRepository.save(review);
    }

    public List<ReviewResponseDTO> getByCafeId(Long cafeId, boolean incluirInactivas, Long currentUserId) {

        List<Review> reviews = incluirInactivas
                ? reviewRepository.findByCafeIdAndEstadoIn(
                cafeId,
                List.of(EstadoReview.ACTIVA, EstadoReview.INACTIVA)
        )
                : reviewRepository.findByCafeIdAndEstado(
                cafeId,
                EstadoReview.ACTIVA
        );
        return mapReviewsConReacciones(reviews, currentUserId);
    }

    public List<ReviewResponseDTO> getByUserId(Long userId, boolean incluirInactivas) {

        List<Review> reviews = incluirInactivas
                ? reviewRepository.findByUsuarioIdAndEstadoIn(
                userId,
                List.of(EstadoReview.ACTIVA, EstadoReview.INACTIVA)
        )
                : reviewRepository.findByUsuarioIdAndEstado(
                userId,
                EstadoReview.ACTIVA
        );

        return mapReviewsConReacciones(reviews, userId);
    }

    private List<ReviewResponseDTO> mapReviewsConReacciones(List<Review> reviews, Long userId) {

        return reviews.stream().map(review -> {

            Long reviewId = review.getId();

            long likes = reaccionRepository.countByReviewIdAndTipo(reviewId, TipoReaccion.LIKE);
            long dislikes = reaccionRepository.countByReviewIdAndTipo(reviewId, TipoReaccion.DISLIKE);

            TipoReaccion reaccionUsuario = null;

            if (userId != null) {
                reaccionUsuario = reaccionRepository
                        .findByUsuarioIdAndReviewId(userId, reviewId) // 🔥 ESTE ES EL CORRECTO
                        .map(LikeReview::getTipo)
                        .orElse(null);
            }

            return reviewMapper.toDTO(review, likes, dislikes, reaccionUsuario);

        }).toList();
    }

    public void reaccionar(Long reviewId, Long userId, TipoReaccion tipo) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review no encontrada"));

        Usuario user = usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        LikeReview existente = reaccionRepository.findByUsuarioIdAndReviewId(userId, reviewId).orElse(null);

        if (existente != null) {
            existente.setTipo(tipo);
            reaccionRepository.save(existente);
            return;
        }

        LikeReview nueva = new LikeReview();
        nueva.setReview(review);
        nueva.setUsuario(user);
        nueva.setTipo(tipo);

        reaccionRepository.save(nueva);
    }

    public void quitarReaccion(Long reviewId, Long userId) {
        reaccionRepository.deleteByUsuarioIdAndReviewId(userId, reviewId);
    }

}
