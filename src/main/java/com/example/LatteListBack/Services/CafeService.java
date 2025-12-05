package com.example.LatteListBack.Services;
import java.time.LocalTime;
import java.time.ZoneId;
import com.example.LatteListBack.DTOs.CoffeDTOs.CafeDetailDTO;
import com.example.LatteListBack.DTOs.CoffeDTOs.CafeFactory;
import com.example.LatteListBack.DTOs.CoffeDTOs.CafeListDTO;
import com.example.LatteListBack.DTOs.CoffeDTOs.CafeMetrics;
import com.example.LatteListBack.Mappers.CafeMapper;
import com.example.LatteListBack.Models.AppStatus;
import com.example.LatteListBack.Models.Cafe;
import com.example.LatteListBack.Models.Review;
import com.example.LatteListBack.Repositorys.AppStatusRepository;
import com.example.LatteListBack.Repositorys.CafeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CafeService {

    private final CafeRepository repo;
    private final GeoapifyService geoapifyService;
    private final CafeMapper cafeMapper;
    private final CafeRatingService ratingService;
    private final AppStatusRepository statusRepo;

    public CafeService(CafeRepository repo, GeoapifyService geoapifyService, CafeMapper cafeMapper, CafeRatingService ratingService, AppStatusRepository statusRepo) {
        this.repo = repo;
        this.geoapifyService = geoapifyService;
        this.cafeMapper = cafeMapper;
        this.ratingService = ratingService;
        this.statusRepo = statusRepo;
    }


    @Transactional
    public List<Cafe> actualizarCafesDesdeApi() {

        AppStatus status = statusRepo.findById("SYNC_STATUS").orElse(new AppStatus());
        LocalDateTime ultimaSync = status.getLastGeoapifySync();

        LocalDateTime ahora = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));

        // Verificar si pasó una semana completa (7 días)
        if (ultimaSync != null && ultimaSync.plusWeeks(1).isAfter(ahora)) {
            System.out.println("⚠️ Saltando Geoapify GET. Última sync: " + ultimaSync);
            // Salimos si NO ha pasado una semana.
            return Collections.emptyList();
        }

        System.out.println("✅ Iniciando Geoapify GET. Ha pasado más de 1 semana o es la primera vez.");

        // Lógica de actualización (lo que ya tenías)
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

        status.setLastGeoapifySync(ahora);
        statusRepo.save(status);
        System.out.println("Sync completada. Nueva fecha de control guardada: " + ahora);

        return repo.saveAll(cafesAGuardar);
    }


    public List<CafeListDTO> listarCafes(
            Boolean delivery,
            Boolean takeaway,
            Boolean internet_access,
            Boolean outdoor_seating,
            Boolean abiertoAhora,
            String search
    ) {

        List<Cafe> todosLosCafes = repo.findAll();

        return todosLosCafes.stream()

                .filter(c -> delivery == null || !delivery || Boolean.TRUE.equals(c.getDelivery()))
                .filter(c -> takeaway == null || !takeaway || Boolean.TRUE.equals(c.getTakeaway()))
                .filter(c -> internet_access == null || !internet_access || Boolean.TRUE.equals(c.getInternetAccess()))
                .filter(c -> outdoor_seating == null || !outdoor_seating || Boolean.TRUE.equals(c.getOutdoorSeating()))
                .filter(c -> abiertoAhora == null || !abiertoAhora || estaAbiertoAhora(c))

                // ⭐ search term
                .filter(c -> search == null || search.isBlank() ||
                        c.getNombre().toLowerCase().contains(search.toLowerCase()))

                .map(CafeFactory::toListDTO)
                .collect(Collectors.toList());
    }

    public CafeDetailDTO buscarPorId(Long id) {

        Cafe cafe = repo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Café no encontrado con id: " + id));

        // 👉 calcular métricas
        CafeMetrics metrics = ratingService.calcular(cafe);

        // 👉 pasar café + métricas al factory
        return CafeFactory.toDetailDTO(cafe, metrics);
    }

    private boolean estaAbiertoAhora(Cafe cafe) {
        String horario = cafe.getOpeningHours();
        if (horario == null || horario.isBlank()) {
            return false;
        }

        // Argentina
        LocalDateTime ahora = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));
        DayOfWeek hoy = ahora.getDayOfWeek();
        LocalTime horaActual = ahora.toLocalTime();

        Map<String, DayOfWeek> mapDias = Map.of(
                "Mo", DayOfWeek.MONDAY,
                "Tu", DayOfWeek.TUESDAY,
                "We", DayOfWeek.WEDNESDAY,
                "Th", DayOfWeek.THURSDAY,
                "Fr", DayOfWeek.FRIDAY,
                "Sa", DayOfWeek.SATURDAY,
                "Su", DayOfWeek.SUNDAY
        );

        // Separar reglas: "Mo-Th,Su 07:00-23:00" | "Fr,Sa 07:00-01:00"
        String[] reglas = horario.split(";");

        for (String regla : reglas) {
            regla = regla.trim();

            // separar días y rango de horas
            String[] partes = regla.split(" ");
            if (partes.length < 2) continue;

            String bloqueDias = partes[0];   // "Mo-Th,Su"
            String rangoHoras = partes[1];   // "07:00-23:00"

            // separar múltiples bloques de días: "Mo-Th" y "Su"
            String[] grupos = bloqueDias.split(",");

            // separar horas
            String[] horas = rangoHoras.split("-");
            if (horas.length != 2) continue;
            LocalTime abre = LocalTime.parse(horas[0]);
            LocalTime cierra = LocalTime.parse(horas[1]);

            // Para cada grupo de días
            for (String g : grupos) {
                g = g.trim();

                // "Mo-Th"
                List<DayOfWeek> diasValidos = new ArrayList<>();

                if (g.contains("-")) {
                    String[] d = g.split("-");
                    DayOfWeek inicio = mapDias.get(d[0]);
                    DayOfWeek fin = mapDias.get(d[1]);

                    DayOfWeek iter = inicio;
                    while (true) {
                        diasValidos.add(iter);
                        if (iter == fin) break;
                        iter = iter.plus(1);
                    }
                }
                // "Su"
                else {
                    diasValidos.add(mapDias.get(g));
                }

                // verifica si hoy corresponde
                if (!diasValidos.contains(hoy)) continue;

                // caso normal: abre y cierra el mismo día
                if (!cierra.isBefore(abre)) {
                    if (!horaActual.isBefore(abre) && !horaActual.isAfter(cierra)) {
                        return true;
                    }
                }
                // caso cierre después de medianoche: ejemplo 07:00-01:00
                else {
                    if (!horaActual.isBefore(abre) || !horaActual.isAfter(cierra)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}