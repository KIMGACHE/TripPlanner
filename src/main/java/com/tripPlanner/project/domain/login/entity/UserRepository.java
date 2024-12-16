package com.tripPlanner.project.domain.login.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    
    //userid 유무 확인
    Optional<UserEntity> findByUserid(String userid);

}
