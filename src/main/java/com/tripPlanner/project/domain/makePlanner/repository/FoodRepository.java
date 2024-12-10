package com.tripPlanner.project.domain.makePlanner.repository;

import com.tripPlanner.project.domain.makePlanner.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food,String>  {
    @Query("SELECT fd FROM Food AS fd WHERE (fd.xCoordinate BETWEEN :xStart AND :xEnd) AND (fd.yCoordinate BETWEEN :yStart AND :yEnd)")
    List<Food> selectFoodAll(
            @Param("xStart") double xStart,
            @Param("yStart") double yStart,
            @Param("xEnd") double xEnd,
            @Param("yEnd") double yEnd
    );
}
