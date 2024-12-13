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

    //    // 검색어로 데이터 요청
//    @GetMapping("/api/getSearchKeyword")
//    public Mono<String> getSearchKeyword(@RequestParam(value = "keyword") String keyword, @RequestParam(value = "pageNo") String pageNo) {
//        return apiService.getSearchKeyword(keyword, pageNo);
//    }
// 검색어로 데이터 요청
    @PostMapping("/api/getSearchKeyword")
    public Mono<String> getSearchKeyword(@RequestBody SearchRequest searchRequest) {
        String keyword = searchRequest.getKeyword();
        String regionCode = searchRequest.getRegionCode();
        String hashtag = searchRequest.getHashtag();
        String pageNo = searchRequest.getPageNo();
        System.out.println("keyword : " + keyword);
        System.out.println("regionCode : " + regionCode);
        System.out.println("hashtag : " +hashtag );
        System.out.println("pageNo : " + pageNo);

        // 모든 값이 비었을 경우
        if (keyword.isEmpty() && regionCode.isEmpty() && hashtag.isEmpty()) {
            System.out.println("모든 값 입력 안 됐을 경우 실행");
            return apiService.getAreaBasedList(regionCode, hashtag, pageNo);
        }

        // keyword만 있을 경우
        if (!keyword.isEmpty() && regionCode.isEmpty() && hashtag.isEmpty()) {
            System.out.println("keyword만 있을 때 호출");
            return apiService.getSearchKeyword(keyword.trim(), pageNo);
        }

        // regionCode나 hashtag만 있을 경우
        if (keyword.isEmpty() && (!regionCode.isEmpty() || !hashtag.isEmpty())) {
            System.out.println("regionCode나 hashtag만 있을 때 호출");
            return apiService.getAreaBasedList(regionCode, hashtag, pageNo);
        }

        System.out.println("모두 있음");
        pageNo = null;
        Mono<String> areaBasedListResult = apiService.getAreaBasedList(regionCode, hashtag, pageNo);
        Mono<String> searchKeywordResult = apiService.getSearchKeyword(keyword.trim(), pageNo);


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


//    // 코스 ID로 상세 데이터 요청
//    @GetMapping("/api/getDetailCommon")
//    public Mono<String> getDetailCommon(@RequestParam(value = "courseId") long courseId, @RequestParam(value = "pageNo") String pageNo) {
//        return apiService.getDetailCommon(courseId, pageNo);
//    }
//
//    // 지역, 해시태그로 데이터 요청
//    @GetMapping("/api/getAreaBasedList")
//    public Mono<String> getAreaBasedList(@RequestParam(value = "regionCode") String regionCode, @RequestParam(name = "hashtag") String hashtag, @RequestParam(value = "pageNo") String pageNo) {
//        return apiService.getAreaBasedList(regionCode, hashtag, pageNo);
//    }
//
//    // 상세 정보 요청
//    @GetMapping("/api/getDetailInfo")
//    public Mono<String> getDetailInfo(@RequestParam(value = "contentId") long contentId, @RequestParam(value = "pageNo") String pageNo) {
//        return apiService.getDetailInfo(contentId, pageNo);
//    }

    // 검색을 할 때 만약 지역이나, 태그를 선택했을 때 Rest요청을 받을 함수
    // 제공 API에 키워드, 지역, 태그를 전부 포함하는 요청이 없어서 직접 구현


}
