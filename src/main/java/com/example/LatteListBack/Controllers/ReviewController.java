package com.example.LatteListBack.Controllers;

import com.example.LatteListBack.DTOs.ReviewDTOs.ReviewRequestDTO;
import com.example.LatteListBack.DTOs.ReviewDTOs.ReviewResponseDTO;
import com.example.LatteListBack.Enums.TipoReaccion;
import com.example.LatteListBack.Repositorys.UserRepository;
import com.example.LatteListBack.Services.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserRepository userRepository;

    public ReviewController(ReviewService reviewService, UserRepository userRepository) {
        this.reviewService = reviewService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ReviewResponseDTO crearReview(@RequestBody ReviewRequestDTO request) {
        System.out.println("ENTRÉ AL CONTROLLER");
        return reviewService.crearReview(request);
    }

    @PutMapping("/{reviewId}")
    public ReviewResponseDTO editarReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewRequestDTO request
    ) {
        return reviewService.editarReview(reviewId, request);
    }

    @DeleteMapping("/{reviewId}")
    public void eliminar(@PathVariable Long reviewId) {
        reviewService.eliminar(reviewId);
    }

    @PatchMapping("/{reviewId}/desactivar")
    public void desactivar(@PathVariable Long reviewId) {
        reviewService.desactivar(reviewId);
    }

    @PatchMapping("/{reviewId}/activar")
    public void activar(@PathVariable Long reviewId) {
        reviewService.activar(reviewId);
    }

    @GetMapping("/cafe/{cafeId}")
    public List<ReviewResponseDTO> getByCafeId(
            @PathVariable Long cafeId,
            @RequestParam(defaultValue = "false") boolean incluirInactivas,
            Principal principal
    ) {
        Long currentUserId = null;

        // 🚨 LÓGICA PARA OBTENER EL ID DEL USUARIO 🚨
        if (principal != null) {
            String username = principal.getName();

            // Buscar el ID del usuario en la base de datos usando el username/email (asumo que es único)
            currentUserId = userRepository.findByEmail(username) // 💡 Usa tu método de búsqueda por username/email
                    .map(user -> user.getId())
                    .orElse(null);

        }
        return reviewService.getByCafeId(cafeId, incluirInactivas, currentUserId);
    }


    @GetMapping("/usuario/{userId}")
    public List<ReviewResponseDTO> getByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "false") boolean incluirInactivas
    ) {
        return reviewService.getByUserId(userId, incluirInactivas);
    }


    @PostMapping("/{reviewId}/reaccion/{userId}")
    public void reaccionar(
            @PathVariable Long reviewId,
            @PathVariable Long userId,
            @RequestParam TipoReaccion tipo
    ) {
        reviewService.reaccionar(reviewId, userId, tipo);
    }

    @DeleteMapping("/{reviewId}/reaccion/{userId}")
    public void quitarReaccion(
            @PathVariable Long reviewId,
            @PathVariable Long userId
    ) {
        reviewService.quitarReaccion(reviewId, userId);
    }
}
