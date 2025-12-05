package com.example.LatteListBack.Repositorys;

import com.example.LatteListBack.Models.AppStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppStatusRepository extends JpaRepository<AppStatus, String> {
}
