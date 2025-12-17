package com.example.LatteListBack.Controllers;

import com.example.LatteListBack.DTOs.ReviewDTOs.ReviewRequestDTO;
import com.example.LatteListBack.DTOs.ReviewDTOs.ReviewResponseDTO;
import com.example.LatteListBack.Enums.TipoReaccion;
import com.example.LatteListBack.Services.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

        @PostMapping
        public ReviewResponseDTO crearReview(@RequestBody ReviewRequestDTO request) {
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
        public List<ReviewResponseDTO> getByCafeId(@PathVariable Long cafeId) {
            return reviewService.getByCafeId(cafeId);
        }

        @GetMapping("/usuario/{userId}")
        public List<ReviewResponseDTO> getByUserId(
                @PathVariable Long userId,
                @RequestParam(defaultValue = "false") boolean incluirInactivas
        ) {
            return reviewService.getByUserId(userId, incluirInactivas);
        }

        @PostMapping("/{reviewId}/reaccion")
        public void reaccionar(
                @PathVariable Long reviewId,
                @RequestParam TipoReaccion tipo
        ) {
            reviewService.reaccionar(reviewId, tipo);
        }

        @DeleteMapping("/{reviewId}/reaccion")
        public void quitarReaccion(@PathVariable Long reviewId) {
            reviewService.quitarReaccion(reviewId);
        }
    }

    /*@PostMapping
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

        if (principal != null) {
            String username = principal.getName();

            currentUserId = userRepository.findByEmail(username) // 💡 Usa tu método de búsqueda por username/email
                    .map(user -> user.getId())
                    .orElse(null);

        }
        return reviewService.getByCafeId(cafeId);
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
        reviewService.reaccionar(reviewId tipo);
    }

    @DeleteMapping("/{reviewId}/reaccion/{userId}")
    public void quitarReaccion(
            @PathVariable Long reviewId,
            @PathVariable Long userId
    ) {
        reviewService.quitarReaccion(reviewId);
    }*/
