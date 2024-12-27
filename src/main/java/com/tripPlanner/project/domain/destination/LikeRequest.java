package com.tripPlanner.project.domain.destination;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeRequest {
    private String plannerID;
    private String userId;
}
