package com.tripPlanner.project.domain.login;

import com.tripPlanner.project.domain.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    //userid 있는지 확인
    boolean existsByUserid(String userid);
    //username 있는지 확인
    boolean existsByUsername(String username);
    Optional<UserEntity> findByUserid(String userid);

}
