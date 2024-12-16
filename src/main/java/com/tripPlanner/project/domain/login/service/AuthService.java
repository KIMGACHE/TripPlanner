package com.tripPlanner.project.domain.login.service;

import com.tripPlanner.project.domain.login.entity.TokenEntity;
import com.tripPlanner.project.domain.login.entity.TokenRepository;
import com.tripPlanner.project.domain.login.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;

    public AuthService(JwtTokenProvider jwtTokenProvider,TokenRepository tokenRepository){
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenRepository = tokenRepository;
    }
    //리프레시 토큰 저장
    public void saveRefreshToken(String userid,String refreshToken,long expiration){
        log.info("토큰 저장..");
        TokenEntity tokenEntity = new TokenEntity(userid,refreshToken,expiration);
        tokenRepository.save(tokenEntity); //redis 저장
    }

    //리프레시 토큰 유효성 검사
    public boolean validateRefreshToken(String userid,String refreshToken){
        log.info("토큰 유효성 검사..");
        Optional<TokenEntity> storedToken = tokenRepository.findById(userid);
        if(storedToken.isPresent()){ //토큰이 존재한다면 비교 후 동일한 이름의 토큰을 가져옴
            log.info("동일한 토큰이 존재합니다 ! ");
            return storedToken.get().getRefreshToken().equals(refreshToken); 
        }

        return false;
    }

    //리프레시 토큰 삭제 (로그아웃 할 때 사용)
    public void deleteRefreshToken(String userid){
        log.info("토큰을 삭제합니다");
        tokenRepository.deleteById(userid); //redis에서 토큰 값 삭제
    }




}
