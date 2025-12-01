package com.example.LatteListBack.Services;

import com.example.LatteListBack.DTOs.CoffeDTOs.CafeFactory;
import com.example.LatteListBack.DTOs.CoffeDTOs.CafeListDTO;
import com.example.LatteListBack.Mappers.CafeMapper;
import com.example.LatteListBack.Models.Cafe;
import com.example.LatteListBack.Models.Review;
import com.example.LatteListBack.Repositorys.CafeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CafeService {

    private final CafeRepository repo;
    private final GeoapifyService geoapifyService;
    private final CafeMapper cafeMapper;
    private final CafeRatingService ratingService;

    public CafeService(CafeRepository repo, GeoapifyService geoapifyService, CafeMapper cafeMapper, CafeRatingService ratingService) {
        this.repo = repo;
        this.geoapifyService = geoapifyService;
        this.cafeMapper = cafeMapper;
        this.ratingService = ratingService;
    }


       @Transactional
        public List<Cafe> actualizarCafesDesdeApi() {

            List<Cafe> cafesApi = geoapifyService.obtenerCafesMdp();
            List<Cafe> cafesAGuardar = new ArrayList<>();

            for (Cafe cafeApi : cafesApi) {

                Optional<Cafe> existenteOpt = repo.findByOsmId(cafeApi.getOsmId());

                if (existenteOpt.isPresent()) {
                    Cafe cafeDB = existenteOpt.get();

                    cafeMapper.copiarDatosDesdeApi(cafeDB, cafeApi);

                    ratingService.calcular(cafeDB);

                    cafesAGuardar.add(cafeDB);

                } else {
                    cafeApi.setResenas(new HashSet<Review>());
                    cafesAGuardar.add(cafeApi);
                }
            }

            return repo.saveAll(cafesAGuardar);
        }


    @Transactional
    public List<CafeListDTO> listarCafes() {

        return repo.findAll()
                .stream()
                .map(CafeFactory::toListDTO)
                .toList();
    }

    }


