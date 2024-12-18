package com.tripPlanner.project.domain.makePlanner.service;

import com.tripPlanner.project.domain.makePlanner.dto.PlannerDto;
import com.tripPlanner.project.domain.makePlanner.entity.Planner;
import com.tripPlanner.project.domain.makePlanner.repository.PlannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PlannerService {
    @Autowired
    private PlannerRepository plannerRepository;

    public Map<String,Object> addPlanner(PlannerDto plannerDto) {
        Map<String,Object> map = new HashMap();

        Planner planner = PlannerDto.dtoToEntity(plannerDto);

        plannerRepository.save(planner);

        return map;
    }
}
