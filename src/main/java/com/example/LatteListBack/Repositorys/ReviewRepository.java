package com.example.LatteListBack.Repositorys;

import com.example.LatteListBack.Models.Cafe;
import com.example.LatteListBack.Models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
