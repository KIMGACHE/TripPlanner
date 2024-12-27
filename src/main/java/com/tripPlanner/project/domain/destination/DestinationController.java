package com.tripPlanner.project.domain.destination;

import com.tripPlanner.project.domain.makePlanner.dto.DestinationDto;
import com.tripPlanner.project.domain.makePlanner.entity.Destination;
import com.tripPlanner.project.domain.makePlanner.repository.DestinationRepository;
import com.tripPlanner.project.domain.makePlanner.service.DestinationService;
import com.tripPlanner.project.domain.tourist.ApiRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class DestinationController {

    private final DestinationRepository destinationRepository;
    private final DestinationService destinationService;

    // 플래너 ID에 해당하는 destination 리스트 반환
    @GetMapping("/planner/board/destination")
    public List<DestinationDto> getDestinationsByPlannerId(@RequestParam("plannerID") int plannerID) {
        List<Destination> destinations = destinationRepository.findByPlanner_PlannerID(plannerID);

        // Destination 엔티티를 DTO로 변환하여 반환
        return destinations.stream()
                .map(Destination::toDto) // Destination을 DestinationDto로 변환
                .collect(Collectors.toList());
    }


    // 좋아요 기능
//    @PostMapping("/like")
//    public ResponseEntity<?> handleLike(@RequestBody LikeRequest likeRequest) {
//        int updatedLikes = destinationService.updateLike(likeRequest.getPlannerID(), likeRequest.getUserId());
//        return ResponseEntity.ok(new LikesResponse(updatedLikes));
//    }

    // destination 리스트 클릭 시 tourist페이지로 가서 정보를 띄워주게 할 contentId를 얻어오기
    @PostMapping("/destination-to-tourist")
    public Mono<String> destinaionToTourist(@RequestBody ApiRequest apiRequest) {
        return destinationService.getLocationBasedList(apiRequest.getMapX(), apiRequest.getMapY());
    }

}
