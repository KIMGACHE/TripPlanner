package com.tripPlanner.project.domain.makePlanner.service;

import com.tripPlanner.project.domain.makePlanner.entity.Destination;
import com.tripPlanner.project.domain.makePlanner.entity.DestinationID;
import com.tripPlanner.project.domain.makePlanner.entity.Planner;
import com.tripPlanner.project.domain.makePlanner.repository.DestinationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class DestinationService {
    @Autowired
    private DestinationRepository destinationRepository;

    @Transactional
    public Map<String,Object> addDestination(Planner planner,int day,List<Map<String,Object>> destination) {
        try {
            Map<String, Object> map = new LinkedHashMap<>();
            for (int i = 1; i <= day; i++) {
                final int dayNumber = i;

                List<Map<String, Object>> list = destination.stream()
                        .filter(el -> (Integer) el.get("day") == dayNumber).toList();
                System.out.println("!!!!!!!!!!!! list : "+list);
                AtomicInteger count = new AtomicInteger(1);
                list.forEach((el) -> {
                    final int index = count.getAndIncrement();
                    Map<String,Object> data = (Map<String,Object>)el.get("data");
                    Destination elements = Destination.builder()
                            .destinationID(new DestinationID(planner.getPlannerID(), (Integer) el.get("day"), index))
                            .name((String)data.get("businessName"))
                            .x((Double)data.get("xCoordinate"))
                            .y((Double)data.get("yCoordinate"))
                            .address((String)data.get("streetFullAddress"))
                            .category((String)data.get("businessCategory"))
                            .image(null)
                            .build();
                    destinationRepository.save(elements);
                });

            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
