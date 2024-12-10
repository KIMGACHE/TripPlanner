package com.tripPlanner.project.domain.makePlanner.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripPlanner.project.domain.makePlanner.dto.FoodDto;
import com.tripPlanner.project.domain.makePlanner.service.AccomService;
import com.tripPlanner.project.domain.makePlanner.dto.AccomDto;
import com.tripPlanner.project.domain.makePlanner.dto.MapDto;
import com.tripPlanner.project.domain.makePlanner.service.FoodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/planner")
public class MainController {
    @Autowired
    private AccomService accomService;

    @Autowired
    private FoodService foodService;

    @GetMapping("/makePlanner")
    public void main(Model model) throws JsonProcessingException {
        log.info("GET /planner/makePlanner...");

        // 최초 좌표 주변의 모든 데이터를 가져온다.
        List<AccomDto> accomList = accomService.test1(126.85320496487589, 35.22056346726476);
        List<FoodDto> foodList = foodService.test1(126.85320496487589, 35.22056346726476);

        // ObjectMapper 객체 생성
        ObjectMapper objectMapper = new ObjectMapper();

        // 자바 객체를 JSON 문자열로 변환
        String accom_json = objectMapper.writeValueAsString(accomList);
        String food_json = objectMapper.writeValueAsString(foodList);

        model.addAttribute("accomList", accom_json);
        model.addAttribute("foodList", food_json);
    }

    @ResponseBody
    @PostMapping("/makePlanner")
    public Map<String, Object> main_post(@RequestBody MapDto mapDto) throws JsonProcessingException {
        log.info("POST /planner/makePlanner...");

        Map<String,Object> datas = new HashMap<>();
        double latitude = mapDto.getLatitude();
        double longitude = mapDto.getLongitude();
        System.out.println("latitude : "+latitude+", longitude : " + longitude);
        // 이동한 좌표 주변의 모든 데이터를 가져온다.
        List<AccomDto> accomList = accomService.test1(longitude, latitude);
        List<FoodDto> foodList = foodService.test1(longitude, latitude);

        // ObjectMapper 객체 생성
        ObjectMapper objectMapper = new ObjectMapper();

        // 자바 객체를 JSON 문자열로 변환
        String accom_json = objectMapper.writeValueAsString(accomList);
        String food_json = objectMapper.writeValueAsString(foodList);

        datas.put("accomList", accom_json);
        datas.put("foodList", food_json);

        return datas;
    }
}
