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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

//    @GetMapping("/makePlanner")
//    public void main(Model model) throws JsonProcessingException {
//        log.info("GET /planner/makePlanner...");
//
//        // 최초 좌표 주변의 모든 데이터를 가져온다.
//        List<AccomDto> accomList = accomService.test1(128.601445, 35.8714354,11);
//        List<FoodDto> foodList = foodService.test1(128.601445, 35.8714354,11);
//
//        // ObjectMapper 객체 생성
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        // 자바 객체를 JSON 문자열로 변환
//        String accom_json = objectMapper.writeValueAsString(accomList);
//        String food_json = objectMapper.writeValueAsString(foodList);
//
//        model.addAttribute("accomList", accom_json);
//        model.addAttribute("foodList", food_json);
//    }

    @ResponseBody
    @PostMapping(value="/makePlanner", consumes = MediaType.APPLICATION_JSON_VALUE, produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> main_post(@RequestBody Map<String,Object> map) throws JsonProcessingException {
        log.info("POST /planner/makePlanner...");

        // 어떤 동작인지 판별하는 값
        String action = (String)map.get("action");

        if(action.equals("addDestination")) {
            String businessName = (String)map.get("businessName");
            String businessCategory = (String)map.get("businessCategory");
            String streetFullAddress = (String)map.get("streetFullAddress");

            System.out.println("businessName : " + businessName + ", businessCategory : " + businessCategory + ", streetFullAddress : " + streetFullAddress);
            return null;
        } else if(action.equals("mapRender")) {
            Map<String,Object> datas = new HashMap<>();
            double latitude = (Double)map.get("latitude");
            double longitude = (Double)map.get("longitude");
            int zoom_level = (Integer)map.get("zoomlevel");

            // 이동한 좌표 주변의 모든 데이터를 가져온다.
            List<AccomDto> accomList = accomService.test1(longitude, latitude,zoom_level);
            List<FoodDto> foodList = foodService.test1(longitude, latitude, zoom_level);

            // ObjectMapper 객체 생성
            ObjectMapper objectMapper = new ObjectMapper();

            // 자바 객체를 JSON 문자열로 변환
            String accom_json = objectMapper.writeValueAsString(accomList);
            String food_json = objectMapper.writeValueAsString(foodList);

            datas.put("accomList", accom_json);
            datas.put("foodList", food_json);

            return new ResponseEntity<Map<String,Object>>(datas, HttpStatus.OK);
        } else {
            System.out.println("error!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

            return null;
        }

    }
}
