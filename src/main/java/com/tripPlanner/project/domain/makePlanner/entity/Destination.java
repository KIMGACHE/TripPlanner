package com.tripPlanner.project.domain.makePlanner.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "tbl_destination")
public class Destination {

    @EmbeddedId
    private DestinationId id;

    @ManyToOne(fetch = FetchType.LAZY)  // 외래 키 관계를 여기서 정의
    @JoinColumn(name = "plannerid", insertable = false, updatable = false)  // 외래 키를 매핑
    private Planner planner;

    @Column(name = "name")
    private String name;

    @Column(name = "coordinate_x")
    private double coordinate_x;

    @Column(name = "coordinate_y")
    private double coordinate_y;
}
