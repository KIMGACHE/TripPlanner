package com.tripPlanner.project.domain.destination;

import com.tripPlanner.project.domain.makePlanner.repository.PlannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final PlannerRepository plannerRepository;

    // 좋아요 상태 및 카운트 조회
    public LikeStatusResponse getLikeStatus(Long plannerID, String userId) {
        boolean isLiked = likeRepository.existsByPlannerIdAndUserId(plannerID, userId);
        int likeCount = likeRepository.countByPlannerId(plannerID);
        return new LikeStatusResponse(isLiked, likeCount);
    }

    // 좋아요 토글 처리
    @Transactional
    public LikeStatusResponse toggleLike(Long plannerID, String userId) {
        boolean isLiked = likeRepository.existsByPlannerIdAndUserId(plannerID, userId);

        if (isLiked) {
            // 좋아요 제거
            likeRepository.deleteByPlannerIdAndUserId(plannerID, userId);
        } else {
            // 좋아요 추가
            Like like = new Like();
            like.setPlannerId(plannerID);
            like.setUserId(userId);
            likeRepository.save(like);
        }

        int likeCount = likeRepository.countByPlannerId(plannerID);
        return new LikeStatusResponse(!isLiked, likeCount);
    }
}
