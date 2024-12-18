package com.tripPlanner.project.domain.makePlanner.dto;

import com.tripPlanner.project.domain.makePlanner.entity.Planner;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlannerDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long plannerid;
    @Column
    private String userid;
    @Column
    private LocalDate createAt;
    @Column
    private LocalDate updateAt;
    @Column
    private int duration;
    @Column
    private int people;


    public static Planner dtoToEntity(PlannerDto plannerDto) {
        return Planner.builder()
                .plannerid(plannerDto.getPlannerid())
                .userid(plannerDto.getUserid())
                .createAt(plannerDto.getCreateAt())
                .updateAt(plannerDto.getUpdateAt())
                .duration(plannerDto.getDuration())
                .people(plannerDto.getPeople())
                .build();
    }

    public static PlannerDto entityToDto(Planner planner) {
        return PlannerDto.builder()
                .plannerid(planner.getPlannerid())
                .userid(planner.getUserid())
                .createAt(planner.getCreateAt())
                .updateAt(planner.getUpdateAt())
                .duration(planner.getDuration())
                .people(planner.getPeople())
                .build();
    }
}
