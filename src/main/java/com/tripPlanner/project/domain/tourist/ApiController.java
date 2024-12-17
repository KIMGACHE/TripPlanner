package com.tripPlanner.project.domain.tourist;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ApiController {

    private final ApiService apiService;


// 검색어로 데이터 요청
    @PostMapping("/api/getSearchKeyword")
    public Mono<String> getSearchKeyword(@RequestBody SearchRequest searchRequest) {
        String keyword = searchRequest.getKeyword();
        String regionCode = searchRequest.getRegionCode();
        String hashtag = searchRequest.getHashtag();
        String pageNo = searchRequest.getPageNo();
        String arrange = searchRequest.getArrange();
//        System.out.println("keyword : " + keyword);
//        System.out.println("regionCode : " + regionCode);
//        System.out.println("hashtag : " + hashtag);
//        System.out.println("pageNo : " + pageNo);

        // 모든 값이 비었을 경우
        if (keyword.isEmpty() && regionCode.isEmpty() && hashtag.isEmpty()) {
            System.out.println("모든 값 입력 안 됐을 경우 실행");
            return apiService.getAreaBasedList(regionCode, hashtag, pageNo, arrange);
        }

        // keyword만 있을 경우
        if (!keyword.isEmpty() && regionCode.isEmpty() && hashtag.isEmpty()) {
            System.out.println("keyword만 있을 때 호출");
            return apiService.getSearchKeyword(keyword.trim(), pageNo, arrange);
        }

        // regionCode나 hashtag만 있을 경우
        if (keyword.isEmpty() && (!regionCode.isEmpty() || !hashtag.isEmpty())) {
            System.out.println("regionCode나 hashtag만 있을 때 호출");
            return apiService.getAreaBasedList(regionCode, hashtag, pageNo, arrange);
        }

        System.out.println("모두 있음");
        pageNo = null;
        Mono<String> areaBasedListResult = apiService.getAreaBasedList(regionCode, hashtag, pageNo, arrange);
        Mono<String> searchKeywordResult = apiService.getSearchKeyword(keyword.trim(), pageNo, arrange);


        return Mono.zip(areaBasedListResult, searchKeywordResult)
                .flatMap(tuple -> {
                    String areaBasedList = tuple.getT1();
                    String searchKeyword = tuple.getT2();

                    // areaBasedList와 searchKeyword를 전달하여 findCommonDataByCat2AndAreaCode를 호출
                    return apiService.findCommonDataByCat2AndAreaCode(areaBasedList, searchKeyword);
                })
                .switchIfEmpty(Mono.just("[]"))  // 데이터가 없을 경우 빈 배열을 반환
                .doOnTerminate(() -> System.out.println("findCommonDataByCat2AndAreaCode 호출 종료"));
    }

    @GetMapping("/travelcourse-info")
    public Mono<String> getTravelCourseInfo(@RequestParam(value = "id") String contentId) {

        String pageNo = "";
        if (!contentId.isEmpty()) {
            return apiService.getDetailInfo(contentId, pageNo);
        }
        return null;
    }

    @GetMapping("/travelcourse-info-detailCommon")
    public Mono<String> getTravelCourseCommons(@RequestParam(value = "id") String contentId) {

        String pageNo = "";
        if (!contentId.isEmpty()) {
            return apiService.getDetailCommon(contentId, pageNo);
        }
        return null;
    }
    @GetMapping("/travelcourse-info-searchKeyword")
    public Mono<String> getTravelCourseInfoSearchKeyword(@RequestParam(value = "keyword") String keyword) {

        String pageNo = "";
        String arrange = "";
        if (!keyword.isEmpty()) {
            return apiService.getSearchKeywordByTourist(keyword, pageNo, arrange);
        }
        return null;
    }

    @GetMapping("/google-search-places")
    public Mono<Map<String, Object>> searchPlaces(@RequestParam(value = "keyword") String keyword) {
        return apiService.searchPlacesByKeyword(keyword);
    }

    @GetMapping("/travelcourse-info-detailIntro")
    public Mono<String> getTravelCourseInfoDetailIntro(@RequestParam(value="id") String contentId){
        String pageNo = "";
        return apiService.getDetailIntro(contentId, pageNo);

    }


}
