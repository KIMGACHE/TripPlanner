package com.tripPlanner.project.domain.tourist;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ApiController {

    private final ApiService apiService;

    @GetMapping("/tourist-info")
    public Mono<String> getTouristInfo(@RequestParam(value = "id") String contentId) {

        if (!contentId.isEmpty()) {
            return apiService.getDetailCommon(contentId);
        }
        return null;
    }

    // 관광지 코스를 띄울 때 여러 필터링을 거쳐 데이터를 표시할 함수 (굳이 이렇게 하는 이유는 API가 제공되지 않기 때문)
    @PostMapping("/api/getSearch")
    public Mono<String> getSearch(@RequestBody ApiRequest apiRequest) {
        String keyword = apiRequest.getKeyword();
        String regionCode = apiRequest.getRegionCode();
        String hashtag = apiRequest.getHashtag();
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
    }

    // 관광지 코스 상세페이지로 진입할 때 contentid를 파라미터로 받아서 맵핑
    @GetMapping("/travelcourse-info")
    public Mono<String> getTravelCourseInfo(@RequestParam(value = "id") String contentId) {

        if (!contentId.isEmpty()) {
            return apiService.getDetailInfo(contentId);
        }
        return null;
    }

    // 관광지 코스 상세페이지
    @PostMapping("/travelcourse-info-detailCommon")
    public Mono<String> getTravelCourseCommons(@RequestBody ApiRequest apiRequest) {
        String contentId = apiRequest.getContentId();
        if (contentId != null && !contentId.isEmpty()) {
            return apiService.getDetailCommon(contentId);
        }
        return Mono.empty();  // null 대신 Mono.empty()를 반환하여 빈 값을 처리
    }
    // 관광지 코스 각각의 장소들에 대한 정보를 구글api로 요청했을 때 데이터가 없으면 요청할 함수 (백업용)
    @PostMapping("/travelcourse-info-searchKeyword")
    public Mono<String> getTravelCourseInfoSearchKeyword(@RequestBody ApiRequest apiRequest) {
        String keyword = apiRequest.getKeyword();

        String pageNo = "";
        String arrange = "";
        if (keyword != null && !keyword.isEmpty()) {
            return apiService.getSearchKeywordByTourist(keyword, pageNo, arrange);
        }
        return Mono.empty();  // null 대신 Mono.empty()를 반환하여 빈 값을 처리
    }

    // 관광지 코스 각각의 장소들의 이름으로 구글 API를 사용해 검색을 해서 이미지와 좌표를 받아올 함수
    @PostMapping("/google-search-places")
    public Mono<Map<String, Object>> searchPlaces(@RequestBody ApiRequest apiRequest) {
        String keyword = apiRequest.getKeyword();
        return apiService.searchPlacesByKeyword(keyword);
    }

}
