package com.tripPlanner.project.domain.destination;

import com.tripPlanner.project.domain.makePlanner.entity.Planner;
import com.tripPlanner.project.domain.signin.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Table(
        name = "likes",
        indexes = {@Index(name = "idx_planner_user", columnList = "plannerId, userId")}
)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "plannerId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) // plannerId 삭제 시 자동으로 Like 삭제
    private Planner plannerId;  // 플래너 ID를 참조

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) // userId 삭제 시 자동으로 Like 삭제
    private UserEntity userId;   // 좋아요를 누른 사용자 ID를 참조

    @CreationTimestamp
    private LocalDateTime createdAt;
}
