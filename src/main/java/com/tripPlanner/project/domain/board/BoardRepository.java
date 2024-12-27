package com.tripPlanner.project.domain.board;

import com.tripPlanner.project.domain.makePlanner.entity.Planner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Planner, Integer> {

    List<Planner> findByIsPublicTrue();
}
