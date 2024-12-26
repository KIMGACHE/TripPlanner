package com.tripPlanner.project.domain.destination;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;

@Service
public class DestinationService {

    private final WebClient webClient;


    @Value("${api.service.key}")
    private String serviceKey;

    public DestinationService(WebClient.Builder webClientBuilder) {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        // 쿼리 파라미터 인코딩을 하지 않도록 설정
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        // WebClient를 생성할 때 이 factory를 사용
        this.webClient = WebClient.builder()
                .uriBuilderFactory(factory)
                .build();
    }

    // x,y 좌표로 데이터 가져오는 기능 (반경 몇미터인지도 정하는거 가능)
    public Mono<String> getLocationBasedList(String x, String y) {
        // URL을 수동으로 구성
        String url = "https://apis.data.go.kr/B551011/KorService1/locationBasedList1"
                + "?serviceKey=" + serviceKey
                + "&pageNo=" + 1
                + "&numOfRows=10"
                + "&MobileApp=AppTest"
                + "&MobileOS=ETC"
                + "&mapX=" + x
                + "&mapY=" + y
                + "&radius=0"
                + "&_type=json";
        System.out.println("url : " + url);
        // WebClient를 사용하여 API 호출
        return webClient.get()
                .uri(url)  // 인코딩 없이 URL을 그대로 전달
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response -> {
                    try {
                        // JSON 응답을 파싱하여 JsonNode로 변환
                        JsonNode responseNode = new ObjectMapper().readTree(response);

                        // body 데이터만 추출하여 반환
                        JsonNode items = responseNode.path("response").path("body");

                        // JsonNode 그대로 반환
                        return Mono.just(items.toString());  // JsonNode를 그대로 JSON 형식의 문자열로 반환

                    } catch (JsonProcessingException e) {
                        return Mono.error(new RuntimeException("Error processing JSON", e));  // JSON 처리 중 오류가 발생하면 에러 반환
                    }
                });
    }
}
