package com.tripPlanner.project.domain.signin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    // 아이디 중복 검사
    Optional<UserEntity> findByUserid(String userid);

}