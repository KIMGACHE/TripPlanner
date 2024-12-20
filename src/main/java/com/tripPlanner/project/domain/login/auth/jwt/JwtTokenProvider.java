package com.tripPlanner.project.domain.login.auth.jwt;

import com.tripPlanner.project.domain.login.entity.TokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

    private final Key key;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;


    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration
    ){
        log.info("JWT Token Provider Init..");
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)); //secret key 를 디코딩 후 HMAC 키 생성
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }
    // 엑세스 토큰 생성 
    public String generateAccessToken(String userid){
        log.info("엑세스 토큰 발급");
        Long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(userid)
                .setIssuedAt(new Date())
                .setExpiration(new Date(now + accessTokenExpiration)) //만료시간 = 현재시간 + 토큰유효기간
                .signWith(key , SignatureAlgorithm.HS256) //HMAC SHA256 알고리즘 사용
                .compact(); //토큰 생성 후 압축
    }
    // 리프레시 토큰 생성
    public String generateRefreshToken(String userId) {
        log.info("리프레시 토큰 발급");
        Long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(userId) // 사용자 ID를 `subject`로 설정
                .setIssuedAt(new Date()) // 생성 시간
                .setExpiration(new Date(now + refreshTokenExpiration)) // 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // HMAC SHA256 알고리즘 사용
                .compact(); // 토큰 생성 후 압축
    }
    public String getUserIdFromToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token) //토큰 파싱 및 검증
                .getBody()
                .getSubject(); //subject 에서 사용자 id 추출함
    }
    



}
