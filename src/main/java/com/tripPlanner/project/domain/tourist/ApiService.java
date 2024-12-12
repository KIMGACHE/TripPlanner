package com.tripPlanner.project.domain.tourist;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import reactor.core.publisher.Mono;

@Service
public class ApiService {

    // contentTypeId : 관광타입 (12:관광지, 14:문화시설, 15:축제공연행사, 25:여행코스, 28:레포츠, 32:숙박, 38:쇼핑, 39:음식점) ID


    private final WebClient webClient;

    @Value("${api.service.key}")
    private String serviceKey;

    public ApiService(WebClient.Builder webClientBuilder) {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        // 쿼리 파라미터 인코딩을 하지 않도록 설정
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        // WebClient를 생성할 때 이 factory를 사용
        this.webClient = WebClient.builder()
                .uriBuilderFactory(factory)
                .build();
    }

    public Mono<String> filterByRegionCode(String result, String regionCode) {
        try {
            // JSON 파싱
            JsonNode rootNode = new ObjectMapper().readTree(result);
            JsonNode itemsNode = rootNode.path("response").path("body").path("items");

            // regionCode에 맞는 데이터 필터링
            ArrayNode filteredResults = new ObjectMapper().createArrayNode();
            for (JsonNode item : itemsNode) {
                String itemRegionCode = item.path("areaCode").asText();
                if (itemRegionCode.equals(regionCode)) {
                    filteredResults.add(item);
                }
            }

            // 필터링된 결과 반환
            ObjectMapper objectMapper = new ObjectMapper();
            return Mono.just(objectMapper.writeValueAsString(filteredResults));

        } catch (JsonProcessingException e) {
            return Mono.error(new RuntimeException("Error parsing JSON", e));
        }
    }
    public Mono<String> filterByHashtag(String result, String hashtag) {
        try {
            // JSON 파싱
            JsonNode rootNode = new ObjectMapper().readTree(result);
            JsonNode itemsNode = rootNode.path("response").path("body").path("items");

            // hashtag에 맞는 데이터 필터링
            ArrayNode filteredResults = new ObjectMapper().createArrayNode();
            for (JsonNode item : itemsNode) {
                String itemHashtags = item.path("hashtag").asText();
                if (itemHashtags.contains(hashtag)) {
                    filteredResults.add(item);
                }
            }

            // 필터링된 결과 반환
            ObjectMapper objectMapper = new ObjectMapper();
            return Mono.just(objectMapper.writeValueAsString(filteredResults));

        } catch (JsonProcessingException e) {
            return Mono.error(new RuntimeException("Error parsing JSON", e));
        }
    }
    public Mono<String> getSearchKeywordWithFilters(String keyword, String pageNo, String regionCode, String hashtag) {
        return getSearchKeyword(keyword, pageNo)
                .flatMap(result -> {
                    // regionCode 필터링
                    if (regionCode != null && !regionCode.isEmpty()) {
                        result = filterByRegionCode(result, regionCode).block();
                    }

                    // hashtag 필터링
                    if (hashtag != null && !hashtag.isEmpty()) {
                        result = filterByHashtag(result, hashtag).block();
                    }

                    return Mono.just(result);
                });
    }


    // 검색어에 맞춰 데이터를 가져오는 함수
    public Mono<String> getSearchKeyword(String keyword, String pageNo) {

        keyword = keyword.trim();
        // URL을 수동으로 구성
        String url = "https://apis.data.go.kr/B551011/KorService1/searchKeyword1"
                + "?serviceKey=" + serviceKey
                + "&pageNo=" + pageNo
                + "&numOfRows=10"
                + "&MobileApp=AppTest"
                + "&MobileOS=ETC"
                + "&arrange=Q"
                + "&contentTypeId=25"
                + "&keyword=" + (keyword != null && !keyword.isEmpty() ? keyword : "")
                + "&_type=json";



        // WebClient를 사용하여 API 호출
        return webClient.get()
                .uri(url)  // 인코딩 없이 URL을 그대로 전달
                .retrieve()
                .bodyToMono(String.class);  // 응답 본문을 Mono로 변환
    }

    // 코스 ID를 바탕으로 상세 데이터를 가져오는 함수
    public Mono<String> getDetailCommon(long courseId, String pageNo) {
        // URL을 수동으로 구성
        String url = "https://apis.data.go.kr/B551011/KorService1/detailCommon1"
                + "?serviceKey=" + serviceKey
                + "&pageNo=" + pageNo
                + "&numOfRows=10"
                + "&MobileApp=AppTest"
                + "&MobileOS=ETC"
                + "&contentId=" + courseId
                + "&contentTypeId=25"
                + "&overviewYN=Y"
                + "&_type=json";

        // WebClient를 사용하여 API 호출
        return webClient.get()
                .uri(url)  // 인코딩 없이 URL을 그대로 전달
                .retrieve()
                .bodyToMono(String.class);
    }

    // 지역 및 해시태그에 맞춰 데이터를 가져오는 함수
    public Mono<String> getAreaBasedList(String regionCode, String hashtag, String pageNo) {
        // URL을 수동으로 구성
        String url = "https://apis.data.go.kr/B551011/KorService1/areaBasedList1"
                + "?serviceKey=" + serviceKey
                + "&pageNo=" + pageNo
                + "&numOfRows=10"
                + "&MobileApp=AppTest"
                + "&MobileOS=ETC"
                + "&arrange=Q"
                + "&areaCode=" + (regionCode != null && !regionCode.isEmpty() ? regionCode : "")
                + "&contentTypeId=25"
                + "&cat1=C01"
                + "&cat2=" + (hashtag != null && !hashtag.isEmpty() ? hashtag : "")
                + "&_type=json";
        System.out.println(url);

        // WebClient를 사용하여 API 호출
        return webClient.get()
                .uri(url)  // 인코딩 없이 URL을 그대로 전달
                .retrieve()
                .bodyToMono(String.class);
    }

    // 상세 정보를 가져오는 함수 (ContentId)
    public Mono<String> getDetailInfo(long contentId, String pageNo) {
        // URL을 수동으로 구성
        String url = "https://apis.data.go.kr/B551011/KorService1/detailInfo1"
                + "?serviceKey=" + serviceKey
                + "&pageNo=" + pageNo
                + "&MobileApp=AppTest"
                + "&MobileOS=ETC"
                + "&_type=json"
                + "&contentId=" + contentId
                + "&contentTypeId=25"
                + "&numOfRows=10";

        // WebClient를 사용하여 API 호출
        return webClient.get()
                .uri(url)  // 인코딩 없이 URL을 그대로 전달
                .retrieve()
                .bodyToMono(String.class);
    }
}
