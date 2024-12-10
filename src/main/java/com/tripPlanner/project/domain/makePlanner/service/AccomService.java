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

    @Autowired 
    private AccomRepository accomRepository;

    public List<AccomDto> test1(double x, double y) {
        double xStart = x-0.005;
        double yStart = y-0.005;
        double xEnd = x+0.005;
        double yEnd = y+0.005;
        List<Accom> accoms = accomRepository.selectMiddle(xStart,yStart,xEnd,yEnd);
        List<AccomDto> list = new ArrayList<AccomDto>();

        accoms.forEach(el->{
            list.add(AccomDto.entityToDto(el));
        });
        System.out.println(list);
        return list;
    }
}
