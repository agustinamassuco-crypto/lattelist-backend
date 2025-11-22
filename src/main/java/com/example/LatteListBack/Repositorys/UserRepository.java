package com.example.LatteListBack.Repositorys;

import com.example.LatteListBack.Models.Cafe;
import com.example.LatteListBack.Models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Long> {
}
