package com.tripPlanner.project.domain.login.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {
    private Key key;
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;
    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

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

    public String generateAccessToken(String userid){
        log.info("엑세스 토큰 발급");
        return Jwts.builder()
                .setSubject(userid)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration)) //만료시간 = 현재시간 + 토큰유효기간
                .signWith(key , SignatureAlgorithm.HS256) //HMAC SHA256 알고리즘 사용
                .compact(); //토큰 생성 후 압축
    }
    // 리프레시 토큰 생성
    public String generateRefreshToken(String userId) {
        log.info("리프레시 토큰 발급");
        return Jwts.builder()
                .setSubject(userId) // 사용자 ID를 `subject`로 설정
                .setIssuedAt(new Date()) // 생성 시간
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration)) // 만료 시간
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
    
    public boolean validateToken(String token){
        try{
            log.info("토큰 유효성 검사 실행");
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token); //토큰 파싱
            return true;
        }catch (ExpiredJwtException e) { // 토큰 만료
            log.error("Expired JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) { // 지원하지 않는 JWT
            log.error("Unsupported JWT token: {}", e.getMessage());
        } catch (MalformedJwtException e) { // 잘못된 형식의 JWT
            log.error("Malformed JWT token: {}", e.getMessage()); //추후에 GlobalExceptionHandler에서 관리하도록 함
        } catch (SignatureException e) { // 서명 검증 실패
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (IllegalArgumentException e) { // 기타 오류
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false; // 유효하지 않은 경우 false 반환
    }

}
