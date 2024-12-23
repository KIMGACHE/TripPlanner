package com.tripPlanner.project.commons.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Table(name = "Planner")
@Entity
@Builder
public class Planner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int plannerID; // 여행 일정 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", nullable = false)
    private UserEntity user; // 사용자

    @Column(name = "createAt", nullable = false, updatable = false)
    private LocalDateTime createAt = LocalDateTime.now(); // 등록일자

    @Column(name = "updateAt", nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now(); // 수정일자

    @Column(name = "day", nullable = false)
    private byte day; // 여행 기간



}
