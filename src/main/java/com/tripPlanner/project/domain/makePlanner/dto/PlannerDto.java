package com.tripPlanner.project.domain.makePlanner.dto;

import com.tripPlanner.project.commons.entity.UserEntity;
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
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlannerDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int plannerID;
    @Column
    private UserEntity user;
    @Column
    private String plannerTitle;
    @Column
    private LocalDateTime createAt;
    @Column
    private LocalDateTime updateAt;
    @Column
    private int day;
    @Column
    private boolean isPublic;
    @Column
    private String description;

    public static Planner dtoToEntity(PlannerDto plannerDto) {
        return Planner.builder()
                .plannerID(plannerDto.getPlannerID())
                .user(plannerDto.getUser())
                .plannerTitle(plannerDto.getPlannerTitle())
                .createAt(plannerDto.getCreateAt())
                .updateAt(plannerDto.getUpdateAt())
                .day(plannerDto.getDay())
                .isPublic(plannerDto.isPublic())
                .description(plannerDto.getDescription())
                .build();
    }

}
