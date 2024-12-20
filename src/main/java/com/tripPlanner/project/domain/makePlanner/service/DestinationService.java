package com.tripPlanner.project.domain.makePlanner.service;

import com.tripPlanner.project.domain.makePlanner.dto.MapDataDto;
import com.tripPlanner.project.domain.makePlanner.entity.Destination;
import com.tripPlanner.project.domain.makePlanner.entity.DestinationId;
import com.tripPlanner.project.domain.makePlanner.repository.DestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DestinationService {
    @Autowired
    private DestinationRepository destinationRepository;

    public Map<String,Object> addPlanner(List<MapDataDto> list) {
        Map<String,Object> map = new HashMap();
        int count = 0;
        for(MapDataDto mapDataDto : list) {
            count++;
            Destination destination = Destination.builder()
                    .id(new DestinationId(null, 1, count))
                    .name(mapDataDto.getBusinessName())
                    .coordinate_x(mapDataDto.getXCoordinate())
                    .coordinate_y(mapDataDto.getYCoordinate())
                    .build();
            destinationRepository.save(destination);
        }
        count =0;
        return null;
    }
}
