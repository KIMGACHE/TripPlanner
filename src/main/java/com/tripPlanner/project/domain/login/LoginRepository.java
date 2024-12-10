package com.tripPlanner.project.domain.login;

import com.tripPlanner.project.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<LoginEntity,String> {

    Optional<LoginEntity> findByUserid(String userid);

    Optional<User> findByid(String username);
}
