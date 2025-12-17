package com.example.LatteListBack.Services;

import com.example.LatteListBack.DTOs.ReviewDTOs.ReviewRequestDTO;
import com.example.LatteListBack.DTOs.ReviewDTOs.ReviewResponseDTO;
import com.example.LatteListBack.Enums.*;
import com.example.LatteListBack.Mappers.ReviewMapper;
import com.example.LatteListBack.Models.Cafe;
import com.example.LatteListBack.Models.LikeReview;
import com.example.LatteListBack.Models.Review;
import com.example.LatteListBack.Models.Usuario;
import com.example.LatteListBack.Repositorys.CafeRepository;
import com.example.LatteListBack.Repositorys.LikeReviewRepository;
import com.example.LatteListBack.Repositorys.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CafeRepository cafeRepository;
    private final LikeReviewRepository reaccionRepository;
    private final ReviewMapper reviewMapper;
    private final UserService userService;

    public ReviewService(ReviewRepository reviewRepository,
                         CafeRepository cafeRepository,
                         LikeReviewRepository reaccionRepository,
                         ReviewMapper reviewMapper,
                         UserService userService) {

        this.reviewRepository = reviewRepository;
        this.cafeRepository = cafeRepository;
        this.reaccionRepository = reaccionRepository;
        this.reviewMapper = reviewMapper;
        this.userService = userService;
    }


    public ReviewResponseDTO editarReview(Long reviewId, ReviewRequestDTO request) {

        Usuario usuario = userService.getUsuarioAutenticado();

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review no encontrada"));

        if (!review.getUsuario().getId().equals(usuario.getId())) {
            throw new IllegalArgumentException("No podés editar una review que no es tuya");
        }

        review.setComentario(request.getComentario());
        review.setPuntuacion(request.getPuntuacion());

        if (request.getCostoPromedio() != null && !request.getCostoPromedio().isBlank()) {
            review.setCostoPromedio(CostoPromedio.valueOf(request.getCostoPromedio()));
        } else {
            review.setCostoPromedio(null);
        }

        //aca hice el cambio 
        List<Etiquetas> nuevasEtiquetas = request.getEtiquetas() != null
                ? request.getEtiquetas().stream()
                .map(texto -> Etiquetas.fromString(texto))
                .toList()
                : new ArrayList<>();

        review.setEtiquetas(new ArrayList<>(nuevasEtiquetas));

        List<String> nuevasFotos = request.getFotos() != null
                ? request.getFotos()
                : new ArrayList<>();
        review.setFotos(new ArrayList<>(nuevasFotos));

        reviewRepository.save(review);

        long likes = reaccionRepository.countByReviewIdAndTipo(reviewId, TipoReaccion.LIKE);
        long dislikes = reaccionRepository.countByReviewIdAndTipo(reviewId, TipoReaccion.DISLIKE);

        TipoReaccion reaccionUsuario = reaccionRepository
                .findByUsuario_IdAndReview_Id(usuario.getId(), reviewId)
                .map(LikeReview::getTipo)
                .orElse(null);

        return reviewMapper.toDTO(review, likes, dislikes, reaccionUsuario);
    }


    public void reaccionar(Long reviewId, TipoReaccion tipo) {

        Usuario user = userService.getUsuarioAutenticado();

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review no encontrada"));

        LikeReview existente = reaccionRepository
                .findByUsuario_IdAndReview_Id(user.getId(), reviewId)
                .orElse(null);

        if (existente != null) {
            existente.setTipo(tipo);
            return;
        }

        LikeReview nueva = new LikeReview();
        nueva.setReview(review);
        nueva.setUsuario(user);
        nueva.setTipo(tipo);

        reaccionRepository.save(nueva);
    }




    public ReviewResponseDTO crearReview(ReviewRequestDTO request) {

        Usuario user = userService.getUsuarioAutenticado();

        Cafe cafe = cafeRepository.findById(request.getCafeId())
                .orElseThrow(() -> new IllegalArgumentException("Café no encontrado"));

        Review review = reviewMapper.toEntity(request, user, cafe);

        reviewRepository.save(review);

        return reviewMapper.toDTO(review, 0L, 0L,
                reaccionRepository.findByUsuario_IdAndReview_Id(request.getUserId(), review.getId())
                        .map(LikeReview::getTipo)
                        .orElse(null)
        );    }


    public void eliminar(Long reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review no encontrada"));

        review.setEstado(EstadoReview.ELIMINADA);
        reviewRepository.save(review);
    }

    @Transactional
    public void eliminarReviewsPorUsuario(Long userId) {
        List<Review> reviews = reviewRepository.findByUsuarioIdAndEstadoIn(
                userId,
                List.of(EstadoReview.ACTIVA, EstadoReview.INACTIVA)
        );
        for (Review r : reviews) {
            r.setEstado(EstadoReview.ELIMINADA);
        }

        reviewRepository.saveAll(reviews);
    }

    public void desactivar(Long reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review no encontrada"));

        review.setEstado(EstadoReview.INACTIVA);
        reviewRepository.save(review);
    }

    public void activar(Long reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review no encontrada"));

        review.setEstado(EstadoReview.ACTIVA);
        reviewRepository.save(review);
    }

    public List<ReviewResponseDTO> getByCafeId(Long cafeId) {
        Usuario usuario=userService.getUsuarioAutenticado();
        List<Review> reviews=new ArrayList<Review>();
        if(usuario.getTipoDeUsuario() == TipoDeUsuario.CLIENTE && usuario.getEstado() == EstadoUsuario.ACTIVO) {
            reviews=reviewRepository.findReviewsVisiblesPorCafe(cafeId);
        }else{
            reviews=reviewRepository.findReviewsParaAdmin(cafeId);
        }
        return mapReviewsConReacciones(reviews);
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

        return mapReviewsConReacciones(reviews);
    }

    private List<ReviewResponseDTO> mapReviewsConReacciones(List<Review> reviews) {

        List<LikeReview> todas = reaccionRepository.findAll();
        Usuario usuario=userService.getUsuarioAutenticado();


        return reviews.stream().map(review -> {
            Long reviewId = review.getId();

            long likes = reaccionRepository.countByReviewIdAndTipo(reviewId, TipoReaccion.LIKE);
            long dislikes = reaccionRepository.countByReviewIdAndTipo(reviewId, TipoReaccion.DISLIKE);

            TipoReaccion reaccionUsuario = null;

            if (usuario.getId()!= null) {
                reaccionUsuario = reaccionRepository
                        .findByUsuario_IdAndReview_Id(usuario.getId(), reviewId)
                        .map(LikeReview::getTipo)
                        .orElse(null);
            }

            return reviewMapper.toDTO(review, likes, dislikes, reaccionUsuario);

        }).toList();
    }

    public void quitarReaccion(Long reviewId) {

        Usuario user = userService.getUsuarioAutenticado();

        reaccionRepository.deleteByUsuario_IdAndReview_Id(
                user.getId(),
                reviewId
        );
    }

}
