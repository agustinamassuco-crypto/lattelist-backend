package com.example.LatteListBack.Models;

import com.example.LatteListBack.Enums.TipoReaccion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "likes_review",
        uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "review_id"})
)
@Getter
@Setter
@NoArgsConstructor
public class LikeReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoReaccion tipo;
}
