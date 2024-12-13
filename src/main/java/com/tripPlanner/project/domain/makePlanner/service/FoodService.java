package com.tripPlanner.project.domain.makePlanner.service;

import com.tripPlanner.project.domain.makePlanner.dto.FoodDto;
import com.tripPlanner.project.domain.makePlanner.entity.Food;
import com.tripPlanner.project.domain.makePlanner.repository.FoodRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FoodService {

    private double zoomLevel[] = {0,0,0,0,0,0,0,0,0,0,0,0,0.0015,0.00055};  // 8~13 zoomlevel의 x,y변동 값

    @Autowired 
    private FoodRepository foodRepository;

    public List<FoodDto> test1(double x, double y,int zoom_level) {
        double xStart = x-zoomLevel[zoom_level];
        double yStart = y-zoomLevel[zoom_level];
        double xEnd = x+zoomLevel[zoom_level];
        double yEnd = y+zoomLevel[zoom_level];

        List<Food> foods = foodRepository.selectFoodAll(xStart,yStart,xEnd,yEnd);

        List<FoodDto> list = new ArrayList<FoodDto>();

        FoodDto foodDto = new FoodDto();
        foods.forEach(el->{
            list.add(foodDto.entityToDto(el));
        });
        return list;
    }
}
