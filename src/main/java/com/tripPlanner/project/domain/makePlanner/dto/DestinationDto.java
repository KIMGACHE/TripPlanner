package com.tripPlanner.project.domain.makePlanner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DestinationDto {
    private Long plannerid;
    private int plan_day;
    private int plan_order;
    private String name;
    private double coordinate_x;
    private double coordinate_y;
}
