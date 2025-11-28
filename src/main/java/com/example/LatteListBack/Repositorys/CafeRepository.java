package com.example.LatteListBack.Repositorys;

import com.example.LatteListBack.Models.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CafeRepository extends JpaRepository<Cafe, Long> {

    Optional<Cafe> findByOsmId(Long osmId);

}
