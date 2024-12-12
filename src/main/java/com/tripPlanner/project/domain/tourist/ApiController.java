package com.tripPlanner.project.domain.tourist;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    @GetMapping("/api/getSearchKeyword")
    public Mono<String> getSearchKeyword(@RequestParam(value = "keyword") String keyword,
                                         @RequestParam(value = "regionCode") String regionCode,
                                         @RequestParam(value = "hashtag") String hashtag,
                                         @RequestParam(value = "pageNo") String pageNo) {
        System.out.println("getSearchKeyword함수 호출");
        System.out.println("keyword : "  + keyword);
        System.out.println("regionCode : " + regionCode );
        System.out.println("hashtag : " + hashtag);
        System.out.println("pageNo : " + pageNo);

        if (keyword == "" && regionCode == "" && hashtag == "") {
            System.out.println("모든 값 입력 안됐을 경우 실행");
            return apiService.getAreaBasedList(regionCode, hashtag, pageNo);
        }
        // keyword만 있을 때는 getSearchKeyword 호출
        if (keyword != "" && !keyword.isEmpty() && (regionCode == "" || regionCode.isEmpty()) && (hashtag == "" || hashtag.isEmpty())) {
            System.out.println("keyword만 있을 때 호출");
            return apiService.getSearchKeyword(keyword, pageNo);
        }

        // regionCode나 hashtag만 있을 때는 getAreaBasedList 호출
        if ((regionCode != "" && !regionCode.isEmpty()) || (hashtag != "" && !hashtag.isEmpty())) {
            System.out.println("regionCode나 hashtag만 있을 때");
            return apiService.getAreaBasedList(regionCode, hashtag, pageNo);
        }
        System.out.println("모두 있음");
        
        // keyword와 regionCode, hashtag 모두 있을 때, keyword로 검색 후 regionCode, hashtag로 필터링
        return apiService.getSearchKeyword(keyword, pageNo)
        
                .flatMap(result -> {
                    // regionCode로 필터링
                    Mono<String> filteredByRegionCode = regionCode != null && !regionCode.isEmpty()
                            ? apiService.filterByRegionCode(result, regionCode)
                            : Mono.just(result); // regionCode가 없으면 원본 결과 그대로 반환

                    // hashtag로 필터링
                    return filteredByRegionCode.flatMap(filteredResult ->
                            (hashtag != null && !hashtag.isEmpty())
                                    ? apiService.filterByHashtag(filteredResult, hashtag)
                                    : Mono.just(filteredResult)
                    );
                });
    }



    // 코스 ID로 상세 데이터 요청
    @GetMapping("/api/getDetailCommon")
    public Mono<String> getDetailCommon(@RequestParam(value = "courseId") long courseId, @RequestParam(value = "pageNo") String pageNo) {
        return apiService.getDetailCommon(courseId, pageNo);
    }

    // 지역, 해시태그로 데이터 요청
    @GetMapping("/api/getAreaBasedList")
    public Mono<String> getAreaBasedList(@RequestParam(value = "regionCode") String regionCode, @RequestParam(name = "hashtag") String hashtag, @RequestParam(value = "pageNo") String pageNo) {
        return apiService.getAreaBasedList(regionCode, hashtag, pageNo);
    }

    // 상세 정보 요청
    @GetMapping("/api/getDetailInfo")
    public Mono<String> getDetailInfo(@RequestParam(value = "contentId") long contentId, @RequestParam(value = "pageNo") String pageNo) {
        return apiService.getDetailInfo(contentId, pageNo);
    }

    // 검색을 할 때 만약 지역이나, 태그를 선택했을 때 Rest요청을 받을 함수
    // 제공 API에 키워드, 지역, 태그를 전부 포함하는 요청이 없어서 직접 구현


}
