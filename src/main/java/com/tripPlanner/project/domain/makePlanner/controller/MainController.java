package com.tripPlanner.project.domain.makePlanner.controller;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripPlanner.project.domain.makePlanner.dto.FoodDto;
import com.tripPlanner.project.domain.makePlanner.dto.MapDataDto;
import com.tripPlanner.project.domain.makePlanner.entity.Planner;
import com.tripPlanner.project.domain.makePlanner.service.AccomService;
import com.tripPlanner.project.domain.makePlanner.dto.AccomDto;
import com.tripPlanner.project.domain.makePlanner.dto.MapDto;
import com.tripPlanner.project.domain.makePlanner.service.DestinationService;
import com.tripPlanner.project.domain.makePlanner.service.FoodService;
import com.tripPlanner.project.domain.makePlanner.service.PlannerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @Autowired
    private DestinationService destinationService;

    @Autowired
    private PlannerService plannerService;

    @ResponseBody
    @PostMapping(value="/findDestination", consumes = MediaType.APPLICATION_JSON_VALUE, produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> find_destination(@RequestBody Map<String,Object> map) throws JsonProcessingException {
        log.info("POST /planner/findDestination...");

        // 값을 담을 map객체
        Map<String,Object> datas = new HashMap<>();

        String businessName = (String)map.get("businessName");
        String businessCategory = (String)map.get("businessCategory");
        String streetFullAddress = (String)map.get("streetFullAddress");
        Double xCoordinate = (Double)map.get("coordinate_x");
        Double yCoordinate = (Double)map.get("coordinate_y");
        datas.put("businessName",businessName);
        datas.put("businessCategory",businessCategory);
        datas.put("streetFullAddress",streetFullAddress);
        datas.put("xCoordinate",xCoordinate);
        datas.put("yCoordinate",yCoordinate);

        return new ResponseEntity<Map<String,Object>>(datas, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value="/listDestination", consumes = MediaType.APPLICATION_JSON_VALUE, produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> list_destination(@RequestBody Map<String,Object> map) throws JsonProcessingException {
        log.info("POST /planner/listDestination...");

        // 값을 담을 map객체
        Map<String,Object> datas = new HashMap<>();

        double latitude = (Double)map.get("latitude");
        double longitude = (Double)map.get("longitude");
        int zoom_level = (Integer)map.get("zoomlevel");

        // 이동한 좌표 주변의 모든 데이터를 가져온다.
        List<AccomDto> accomList = accomService.listAccom(longitude, latitude,zoom_level);
        List<FoodDto> foodList = foodService.test1(longitude, latitude, zoom_level);

        // ObjectMapper 객체 생성
        ObjectMapper objectMapper = new ObjectMapper();

        // 자바 객체를 JSON 문자열로 변환
        String accom_json = objectMapper.writeValueAsString(accomList);
        String food_json = objectMapper.writeValueAsString(foodList);

        datas.put("accomList", accom_json);
        datas.put("foodList", food_json);

        return new ResponseEntity(datas, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value="/addPlanner", consumes = MediaType.APPLICATION_JSON_VALUE, produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,Object>> add_planner(@RequestBody Map<String,Object> map) {
        String title = (String)map.get("title");
        String description = (String)map.get("description");
        boolean isPublic = (Boolean)map.get("isPublic");
        int day = (Integer)map.get("day");
        ArrayList<Map<String,Object>> destination = (ArrayList<Map<String,Object>>)map.get("destination");

        log.info("POST /planner/addPlanner..."+destination);

        Planner planner = plannerService.addPlanner(title,description,day,isPublic);
        Map<String,Object> datas = destinationService.addDestination(planner, day, destination);

        return new ResponseEntity(null, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value="/searchDestination", consumes = MediaType.APPLICATION_JSON_VALUE, produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,Object>> search_destination(@RequestBody Map<String,Object> map) {
        String type = (String)map.get("type");
        String word = (String)map.get("word");
        String areaname = (String)map.get("areaname");

        Map<String,Object> datas = new HashMap<>();
        log.info("POST /planner/searchDestination..."+word);

        if(type.equals("식당")) {
            List<FoodDto> searchList = foodService.searchFood(word,areaname);
            datas.put("data",searchList);
        } else if(type.equals("숙소")) {
//            accomService
        } else if(type.equals("관광지")) {

        } else {
            System.out.println("error");
        }


        return new ResponseEntity(datas, HttpStatus.OK);
    }
}
