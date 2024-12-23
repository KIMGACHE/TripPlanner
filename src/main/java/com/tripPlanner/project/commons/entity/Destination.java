package com.tripPlanner.project.commons.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@AllArgsConstructor
@Table(name = "Destination")
@Builder
public class Destination {

    @EmbeddedId
    private DestinationId destinationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plannerID", referencedColumnName = "plannerID", insertable = false, updatable = false)
    private Planner planner; // Planner와의 관계 설정 (외래 키)

    @Column(name = "name")
    private String name; // 장소명 (nullable)

    @Column(name = "x", nullable = false)
    private double x; // 경도

    @Column(name = "y", nullable = false)
    private double y; // 위도

    @Column(name = "address")
    private String address; // 주소

    @Column(name = "category")
    private String category; // 카테고리
}
