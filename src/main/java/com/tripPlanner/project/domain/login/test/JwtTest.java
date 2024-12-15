package com.tripPlanner.project.domain.login.test;


import com.tripPlanner.project.domain.login.jwt.JwtTokenProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class JwtTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void setUp() {
        // 테스트용 Secret Key
        String secretKey = "test-secret-key-test-secret-key-test-secret-key";
        jwtTokenProvider = new JwtTokenProvider(secretKey, 1800000, 604800000);
    }

    @Test
    public void testGenerateAccessToken() {
        String userId = "user123";
        String token = jwtTokenProvider.generateAccessToken(userId);
        assertNotNull(token); // 토큰이 null이 아닌지 확인
    }

    @Test
    public void testValidateToken() {
        String userId = "user123";
        String token = jwtTokenProvider.generateAccessToken(userId);

        assertTrue(jwtTokenProvider.validateToken(token)); // 유효한 토큰인지 확인
    }

    @Test
    public void testGetUserIdFromToken() {
        String userId = "user123";
        String token = jwtTokenProvider.generateAccessToken(userId);

        String extractedUserId = jwtTokenProvider.getUserIdFromToken(token);
        assertEquals(userId, extractedUserId); // 사용자 ID가 정확히 추출되었는지 확인
    }

    @Test
    public void testExpiredToken() throws InterruptedException {
        // 1초짜리 짧은 유효기간의 토큰 생성
        String userId = "user123";
        String expiredToken = Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis() + 1000)) // 1초 후 만료
                .signWith(Keys.hmacShaKeyFor("test-secret-key-test-secret-key-test-secret-key".getBytes()), SignatureAlgorithm.HS256)
                .compact();

        Thread.sleep(2000); // 2초 대기하여 토큰 만료

        assertFalse(jwtTokenProvider.validateToken(expiredToken)); // 만료된 토큰은 유효하지 않아야 함
    }
}
