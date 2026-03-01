package com.Project.TalentConnect.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "GigTable")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GigEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private String category;

    @Enumerated(EnumType.STRING)
    private GigStatus status = GigStatus.ACTIVE;

    @ManyToOne
    @JoinColumn(name = "freelancer_id", nullable = false)
    private UserEntity freelancer;

    private LocalDateTime createdAt = LocalDateTime.now();
}
