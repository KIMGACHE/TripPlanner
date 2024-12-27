package com.tripPlanner.project.domain.login.service;

import com.tripPlanner.project.domain.login.auth.jwt.JwtTokenProvider;
import com.tripPlanner.project.domain.login.dto.LoginResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String,Object> redisTemplate;

    public LoginResponse refreshAccessToken(Authentication authentication, String refreshToken){
        try{
            // 엑세스 토큰 재검증 및 재생성
            String newAccessToken = jwtTokenProvider.regenAccessToken(authentication,refreshToken);
            
            return LoginResponse.builder()
                    .success(true)
                    .message("새로운 엑세스 토큰이 발급되었습니다")
                    .accessToken(newAccessToken)
                    .refreshToken(refreshToken) //기존 리프레시 토큰 반환
                    .build();
        }catch(IllegalArgumentException e){
            log.warn("리프레시 토큰이 만료되었거나 유효하지 않습니다",e.getMessage());
            return LoginResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build();
        }
    }

    //쿠키 저장 메서드
    public void setTokenCookies(HttpServletResponse response,String accessToken){
        Cookie accessTokenCookie = new Cookie("accessToken",accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(30 * 60); //30분  ,,1분

        response.addCookie(accessTokenCookie); //쿠키 반환

    }
//
//    //사용자 정보 조회
//    private PrincipalDetail getPrincipalDetails(String userid){
//        try{
//            return (PrincipalDetail) userDetailsService.loadUserByUsername(userid);
//        }catch(Exception e){
//            log.warn("유저를 찾을 수 없습니다",userid ,e.getMessage());
//            throw new IllegalArgumentException("찾을 수 없는 userid");
//        }
//    }
//
//    private Authentication getAuthentication(String token){
//        Claims claims = get
//    }
//
//    private Claims getClaims(String token){
//        return Jwts.parserBuilder()
//                .setSigningKey(jwtTokenProvider.)
//    }
//






}
