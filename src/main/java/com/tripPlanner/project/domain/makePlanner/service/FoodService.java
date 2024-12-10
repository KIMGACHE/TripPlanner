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

    @Autowired 
    private FoodRepository foodRepository;

    public List<FoodDto> test1(double x, double y) {
        double xStart = x-0.005;
        double yStart = y-0.005;
        double xEnd = x+0.005;
        double yEnd = y+0.005;
        List<Food> foods = foodRepository.selectFoodAll(xStart,yStart,xEnd,yEnd);
//        Optional<Food> food = foodRepository.findById("3000000-101-1899-10737");

//        System.out.println("제ㅔ에에에에에에에발"+food.get());
        List<FoodDto> list = new ArrayList<FoodDto>();

        FoodDto foodDto = new FoodDto();
        foods.forEach(el->{
            list.add(foodDto.entityToDto(el));
        });
        log.info("Food Service : ", list);
        return list;
    }
}
