package com.tripPlanner.project.domain.makePlanner.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripPlanner.project.domain.makePlanner.dto.FoodDto;
import com.tripPlanner.project.domain.makePlanner.entity.Planner;
import com.tripPlanner.project.domain.makePlanner.service.*;
import com.tripPlanner.project.domain.makePlanner.dto.AccomDto;
import com.tripPlanner.project.domain.tourist.ApiRequest;
import com.tripPlanner.project.domain.tourist.ApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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

    @Autowired
    private PlannerApiService plannerApiService;

    @ResponseBody
    @PostMapping(value="/getImages", consumes = MediaType.APPLICATION_JSON_VALUE, produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getImages(@RequestBody Map<String,Object> map) throws JsonProcessingException {
        log.info("POST /planner/getImages...");

        // 값을 담을 map객체
        Map<String,Object> datas = new HashMap<>();

        String businessName = (String)map.get("businessName");

//        System.out.println("businessName : "+businessName);

        datas.put("image",plannerApiService.getPlaceImage(businessName).block());

        return new ResponseEntity<Map<String,Object>>(datas, HttpStatus.OK);
    }

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
        List<FoodDto> foodList = foodService.listFood(longitude, latitude, zoom_level);

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
        String areaName = (String)map.get("areaName");
        String description = (String)map.get("description");
        boolean isPublic = (Boolean)map.get("isPublic");
        int day = (Integer)map.get("day");
        String userid = (String)map.get("userid");
        ArrayList<Map<String,Object>> destination = (ArrayList<Map<String,Object>>)map.get("destination");

        log.info("POST /planner/addPlanner...");

        Planner planner = plannerService.addPlanner(title,areaName,description,day,isPublic,userid);
        Map<String,Object> datas = destinationService.addDestination(planner, day, destination);

        return new ResponseEntity(datas, HttpStatus.OK);
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
            List<AccomDto> searchList = accomService.searchAccom(word,areaname);
            datas.put("data",searchList);
        } else if(type.equals("관광지")) {
            System.out.println("호출");
            String keyword = apiRequest.getKeyword();
            String regionCode = apiRequest.getRegionCode();
            String hashtag = null;
            String pageNo = apiRequest.getPageNo();
            String arrange = apiRequest.getArrange();
            String contentTypeId = apiRequest.getContentTypeId();

            // 모든 값이 비었을 경우
            if (keyword.isEmpty() && regionCode.isEmpty() && hashtag.isEmpty()) {
                System.out.println("모든 값 비었을 때 반응");
                return apiService.getAreaBasedList(regionCode, hashtag, pageNo, arrange, contentTypeId);
            }

            // keyword만 있을 경우
            if (!keyword.isEmpty() && regionCode.isEmpty() && hashtag.isEmpty()) {
                System.out.println("keyword만 있을 때 반응");
                return apiService.getSearchKeyword(keyword.trim(), pageNo, arrange, contentTypeId);
            }

            // regionCode나 hashtag만 있을 경우
            if (keyword.isEmpty() && (!regionCode.isEmpty() || !hashtag.isEmpty())) {
                System.out.println("지역 코드나 카테고리만 있을 때 반응");
                return apiService.getAreaBasedList(regionCode, hashtag, pageNo, arrange, contentTypeId);
            }

            System.out.println("다 없음");
            Mono<String> areaBasedListResult = apiService.getAreaBasedList(regionCode, hashtag, pageNo, arrange, contentTypeId);
            Mono<String> searchKeywordResult = apiService.getSearchKeyword(keyword.trim(), pageNo, arrange, contentTypeId);


            return Mono.zip(areaBasedListResult, searchKeywordResult)
                    .flatMap(tuple -> {
                        String areaBasedList = tuple.getT1();
                        String searchKeyword = tuple.getT2();

                        // areaBasedList와 searchKeyword를 전달하여 findCommonDataByCat2AndAreaCode를 호출
                        return apiService.findCommonDataByCat2AndAreaCode(areaBasedList, searchKeyword);
                    })
                    .switchIfEmpty(Mono.just("[]"))  // 데이터가 없을 경우 빈 배열을 반환
                    .doOnTerminate(() -> System.out.println("findCommonDataByCat2AndAreaCode 호출 종료"));
        } else {
            System.out.println("error");
        }


        return new ResponseEntity(datas, HttpStatus.OK);
    }

    // 관광지 코스를 띄울 때 여러 필터링을 거쳐 데이터를 표시할 함수 (굳이 이렇게 하는 이유는 API가 제공되지 않기 때문)
    @PostMapping("/getSearch")
    public Mono<String> getSearch(@RequestBody ApiRequest apiRequest) {

    }
}
// f57%2FvzD0xikhY%2BT%2FUp%2BhJY6yczlZKsLfk6F3HJXBuefh4KUKuEtmV0kc%2Bcf7shvdxz0s%2FHYIvbO6yHn1NVJ7EA%3D%3D
