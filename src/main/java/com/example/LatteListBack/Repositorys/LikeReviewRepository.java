package com.example.LatteListBack.Repositorys;

import com.example.LatteListBack.Models.LikeReview;
import com.example.LatteListBack.Enums.TipoReaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikeReviewRepository extends JpaRepository<LikeReview, Long> {

    @Query("SELECT COUNT(l) FROM LikeReview l WHERE l.review.id = :reviewId AND l.tipo = :tipo")
    Long countByReviewIdAndTipo(Long reviewId, TipoReaccion tipo);

    Optional<LikeReview> findByUsuarioIdAndReviewId(Long userId, Long id);

    void deleteByUsuarioIdAndReviewId(Long userId, Long reviewId);
}
