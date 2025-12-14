package com.example.LatteListBack.Services;

import com.example.LatteListBack.Enums.TipoReaccion;
import com.example.LatteListBack.Models.LikeReview;
import com.example.LatteListBack.Models.Review;
import com.example.LatteListBack.Models.Usuario;

import com.example.LatteListBack.Repositorys.LikeReviewRepository;
import com.example.LatteListBack.Repositorys.ReviewRepository;
import com.example.LatteListBack.Repositorys.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeReviewService {

    private final LikeReviewRepository likeRepo;
    private final UserRepository usuarioRepo;
    private final ReviewRepository reviewRepo;

    public LikeReviewService(LikeReviewRepository likeRepo,
                             UserRepository usuarioRepo,
                             ReviewRepository reviewRepo) {
        this.likeRepo = likeRepo;
        this.usuarioRepo = usuarioRepo;
        this.reviewRepo = reviewRepo;
    }

    @Transactional
    public LikeReview reaccionar(Long usuarioId, Long reviewId, TipoReaccion tipo) {

        Usuario usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review no encontrada"));

        var existente = likeRepo.findByUsuarioIdAndReviewId(usuarioId, reviewId);

        if (existente.isPresent()) {
            LikeReview like = existente.get();

            if (like.getTipo() == tipo) {
                return like;
            }

            like.setTipo(tipo);
            return likeRepo.save(like);
        }

        LikeReview nuevo = new LikeReview();
        nuevo.setUsuario(usuario);
        nuevo.setReview(review);
        nuevo.setTipo(tipo);

        return likeRepo.save(nuevo);
    }

    @Transactional
    public void quitarReaccion(Long usuarioId, Long reviewId) {
        var existente = likeRepo.findByUsuarioIdAndReviewId(usuarioId, reviewId);

        if (existente.isPresent()) {
            likeRepo.delete(existente.get());
        }
    }

    public Long contarLikes(Long reviewId) {
        return likeRepo.countByReviewIdAndTipo(reviewId, TipoReaccion.LIKE);
    }

    public Long contarDislikes(Long reviewId) {
        return likeRepo.countByReviewIdAndTipo(reviewId, TipoReaccion.DISLIKE);
    }

    public TipoReaccion reaccionUsuario(Long usuarioId, Long reviewId) {
        return likeRepo
                .findByUsuarioIdAndReviewId(usuarioId, reviewId)
                .map(LikeReview::getTipo)
                .orElse(null);
    }
}
