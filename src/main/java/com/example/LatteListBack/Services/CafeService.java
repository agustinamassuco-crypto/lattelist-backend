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
import java.util.stream.Collectors;

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
    public List<CafeListDTO> listarCafes(
            //  filtro del frontend
            Boolean delivery,
            Boolean takeaway,
            Boolean internet_access,
            Boolean outdoor_seating,
            Boolean abiertoAhora) {

        List<Cafe> todosLosCafes = repo.findAll();

        return todosLosCafes.stream()

                .filter(c -> delivery == null || !delivery || (c.getDelivery() != null && c.getDelivery()))

                // Filtro 2: Takeaway
                .filter(c -> takeaway == null || !takeaway || (c.getTakeaway() != null && c.getTakeaway()))

                // Filtro 3: Internet Access (Robustecido)
                .filter(c -> internet_access == null || !internet_access || (c.getInternetAccess() != null && c.getInternetAccess()))

                // Filtro 4: Outdoor Seating (Robustecido)
                .filter(c -> outdoor_seating == null || !outdoor_seating || (c.getOutdoorSeating() != null && c.getOutdoorSeating()))

                // Filtro 5: Abierto Ahora (Robustecido)
                .filter(c -> abiertoAhora == null || !abiertoAhora || estaAbiertoAhora(c))

                // 3. Mapear y recolectar el resultado
                .map(CafeFactory::toListDTO)
                .collect(Collectors.toList());
    }


    private boolean estaAbiertoAhora(Cafe cafe) {
        /*hay que hacerloooooooooo no anda jaja*/


        return cafe.getDelivery() != null && cafe.getDelivery();
    }
}