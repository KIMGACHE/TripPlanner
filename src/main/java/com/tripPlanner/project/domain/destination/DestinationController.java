package com.tripPlanner.project.domain.destination;

import com.tripPlanner.project.commons.entity.Destination;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class DestinationController {

    private final DestinationRepository destinationRepository;

    // 플래너 ID에 해당하는 destination 리스트 반환
    @GetMapping("/planner/board/destination")
    public List<DestinationDto> getDestinationsByPlannerId(@RequestParam("plannerID") int plannerID) {
        List<Destination> destinations = destinationRepository.findByPlanner_PlannerID(plannerID);

        // Destination 엔티티를 DTO로 변환하여 반환
        return destinations.stream()
                .map(Destination::toDto) // Destination을 DestinationDto로 변환
                .collect(Collectors.toList());
    }
}
