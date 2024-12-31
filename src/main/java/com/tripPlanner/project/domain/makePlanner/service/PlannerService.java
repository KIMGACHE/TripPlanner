package com.tripPlanner.project.domain.makePlanner.service;

import com.tripPlanner.project.domain.makePlanner.dto.PlannerDto;
import com.tripPlanner.project.domain.makePlanner.entity.Planner;
import com.tripPlanner.project.domain.makePlanner.repository.PlannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PlannerService {
    @Autowired
    private PlannerRepository plannerRepository;

    @Transactional
    public Planner addPlanner(String title,String areaName,String description,int day,boolean isPublic) {
        try {
            
            if(title.isEmpty() || description.isEmpty() || day<=0) {
                throw new SQLException("ㅋㅋㅋㅋ");
            }
            
            PlannerDto plannerDto = PlannerDto.builder()
                    .plannerID(0)
                    .user(null)
                    .plannerTitle(title)
                    .createAt(LocalDateTime.now())
                    .updateAt(LocalDateTime.now())
                    .day(day)
                    .isPublic(isPublic)
                    .description(description)
                    .area(areaName)
                    .build();
            Planner planner = PlannerDto.dtoToEntity(plannerDto);

            Planner outcomes = plannerRepository.save(planner);
            return outcomes;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
