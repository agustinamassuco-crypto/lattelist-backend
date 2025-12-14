package com.example.LatteListBack.Repositorys;

import com.example.LatteListBack.Models.ListaDeCafes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListaDeCafesRepository extends JpaRepository<ListaDeCafes, Long> {
    List<ListaDeCafes> findByUsuario_Email(String email);
}
