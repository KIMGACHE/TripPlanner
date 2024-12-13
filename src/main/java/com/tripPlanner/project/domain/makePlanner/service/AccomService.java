package com.tripPlanner.project.domain.makePlanner.service;

import com.tripPlanner.project.domain.makePlanner.dto.AccomDto;
import com.tripPlanner.project.domain.makePlanner.entity.Accom;
import com.tripPlanner.project.domain.makePlanner.repository.AccomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AccomService {

    private double zoomLevel[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0.00005};  // 4~13 zoomlevel의 x,y변동 값

    @Autowired 
    private AccomRepository accomRepository;

    public List<AccomDto> test1(double x, double y, int zoom_level) {
        double xStart = x-zoomLevel[zoom_level];
        double yStart = y-zoomLevel[zoom_level];
        double xEnd = x+zoomLevel[zoom_level];
        double yEnd = y+zoomLevel[zoom_level];
        List<Accom> accoms = accomRepository.selectMiddle(xStart,yStart,xEnd,yEnd);
        List<AccomDto> list = new ArrayList<AccomDto>();

        accoms.forEach(el->{
            list.add(AccomDto.entityToDto(el));
        });
        return list;
    }
}
