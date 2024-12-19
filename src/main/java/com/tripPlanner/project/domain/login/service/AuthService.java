package com.tripPlanner.project.domain.login.service;

import com.tripPlanner.project.domain.login.entity.TokenRepository;
import com.tripPlanner.project.domain.login.auth.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;

    public AuthService(JwtTokenProvider jwtTokenProvider,TokenRepository tokenRepository){
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public void saveRefreshToken(){

    }




}
