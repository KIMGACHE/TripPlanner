package com.tripPlanner.project.domain.destination;


import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByPlannerIdAndUserId(Long plannerID, String userId);

    void deleteByPlannerIdAndUserId(Long plannerID, String userId);

    int countByPlannerId(Long plannerID);
}
