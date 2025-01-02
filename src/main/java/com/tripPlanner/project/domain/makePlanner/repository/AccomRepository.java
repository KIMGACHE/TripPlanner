package com.tripPlanner.project.domain.makePlanner.repository;

import com.tripPlanner.project.domain.makePlanner.entity.Accom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccomRepository extends JpaRepository<Accom,String> {
    // 중심좌표를 기준으로 특정 지역의 Accom만을 조회하는 쿼리문 (zoom level에 따른 것도 해야함)
    @Query("SELECT ac FROM Accom AS ac WHERE (ac.xCoordinate BETWEEN :xStart AND :xEnd) AND (ac.yCoordinate BETWEEN :yStart AND :yEnd)")
    List<Accom> selectMiddle(
            @Param("xStart") double xStart,
            @Param("yStart") double yStart,
            @Param("xEnd") double xEnd,
            @Param("yEnd") double yEnd
    );

}
