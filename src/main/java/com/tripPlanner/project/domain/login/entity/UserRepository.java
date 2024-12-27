package com.tripPlanner.project.domain.login.entity;

import com.tripPlanner.project.commons.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,String> {

    
    //userid 유무 확인
    Optional<UserEntity> findByUserid(String userid);

}
