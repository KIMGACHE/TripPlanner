package com.tripPlanner.project.domain.makePlanner.service;

import com.tripPlanner.project.domain.makePlanner.dto.PlannerDto;
import com.tripPlanner.project.domain.makePlanner.entity.Planner;
import com.tripPlanner.project.domain.makePlanner.repository.PlannerRepository;
import com.tripPlanner.project.domain.signin.entity.UserEntity;
import com.tripPlanner.project.domain.signin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PlannerService {
    @Autowired
    private PlannerRepository plannerRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Planner addPlanner(String title,String areaName,String description,int day,boolean isPublic,String userid) {
        try {
            if(title.isEmpty() || description.isEmpty() || day<=0 || userid.isEmpty()) {
                throw new Exception("에러가 발생했습니다.");
            }

            Optional<UserEntity> temp_user = userRepository.findByUserid(userid);
            UserEntity user = null;

            if(temp_user.isEmpty()) {
                throw new Exception("유저가 존재하지 않습니다.");
            } else {
                user = temp_user.get();
            }

            PlannerDto plannerDto = PlannerDto.builder()
                    .plannerID(0)
                    .user(user)
                    .plannerTitle(title)
                    .createAt(LocalDateTime.now())
                    .updateAt(LocalDateTime.now())
                    .day(day)
                    .isPublic(isPublic)
                    .description(description)
                    .area(areaName)
                    .build();
            Planner planner = PlannerDto.dtoToEntity(plannerDto);

            Planner outcomes = plannerRepository.save(planner);
            return outcomes;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
