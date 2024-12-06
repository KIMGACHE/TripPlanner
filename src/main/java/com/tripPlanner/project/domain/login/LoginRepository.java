package com.tripPlanner.project.domain.login;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<LoginEntity,String> {

    Optional<LoginEntity> findByLoginInfo(LoginDto loginDto);

}
