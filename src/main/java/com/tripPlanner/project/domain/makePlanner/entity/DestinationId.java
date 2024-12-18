package com.tripPlanner.project.domain.makePlanner.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class DestinationId implements Serializable {

    @Column(name = "plannerid")
    private Long plannerid;  // 외래 키는 여기서 단순히 Long 타입으로 정의

    @Column(name = "plan_day")
    private int plan_day;

    @Column(name = "plan_order")
    private int plan_order;
}