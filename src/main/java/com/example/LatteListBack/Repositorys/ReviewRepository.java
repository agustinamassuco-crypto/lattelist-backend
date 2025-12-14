package com.example.LatteListBack.Repositorys;

import com.example.LatteListBack.Enums.EstadoReview;
import com.example.LatteListBack.Models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {


    @Query("SELECT r FROM Review r WHERE r.cafe.id = :cafeId AND r.estado = :estado")
    List<Review> findByCafeIdAndEstado(@Param("cafeId") Long cafeId, @Param("estado") EstadoReview estado);

    List<Review> findByUsuarioIdAndEstado(Long usuarioId, EstadoReview estado);

    List<Review> findByUsuarioIdAndEstadoIn(Long usuarioId, List<EstadoReview> estados);

    List<Review> findByCafeIdAndEstadoIn(Long cafeId, List<EstadoReview> estados);
}
