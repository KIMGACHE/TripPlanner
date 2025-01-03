package com.tripPlanner.project.domain.destination;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Table(name = "likes")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long plannerId;  // 플래너 ID

    private String userId;   // 좋아요를 누른 사용자 ID

    @CreationTimestamp
    private LocalDateTime createdAt;
}