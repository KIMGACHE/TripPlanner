package com.tripPlanner.project.domain.login.auth.jwt;

import com.tripPlanner.project.domain.login.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String,String> redisTemplate;
    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("jwt Filter on..");
        // 토큰 추출
        String token = resolveToken(request);

        if (token == null){
            log.info("JWT 토큰이 null입니다. 다음 필터로 이동");
            filterChain.doFilter(request,response);
            return ;
        }

        log.info("추출된 토큰 정보 : {}", token);

        if(jwtTokenProvider.validateToken(token)){

            Authentication authentication = jwtTokenProvider.getTokenInfo(token);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("어데티케이션 {}", authentication);
        }else{
            log.warn("만료 된 토큰입니다. 리프레시 토큰을 확인합니다");

            String refreshToken = resolveRefreshToken(request);
            Authentication authentication = jwtTokenProvider.getTokenInfo(refreshToken);
            var loginResponse = authService.refreshAccessToken(authentication, refreshToken);
            
            //토큰발급이 성공할 시에
            if(loginResponse.isSuccess()){
                authService.setTokenCookies(response,loginResponse.getAccessToken());
            }

        }

        filterChain.doFilter(request,response);
    }

    // 쿠키에서 토큰 정보 추출
    private String getTokenFromCookies(HttpServletRequest request){
        if (request.getCookies() != null){ 
            for (Cookie cookie : request.getCookies()){
                log.info("쿠키 이름: {}, 쿠키 값: {}", cookie.getName(), cookie.getValue());
                if("accessToken".equals(cookie.getName())){
                    return cookie.getValue(); //쿠키 값 추출
                }
            }
        }
        log.info("쿠키에 값이 존재하지 않습니다");
        return null;
    }
    
    //Request 헤더로부터 토큰 정보 추출
    private String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            log.info("헤더에서 추출된 토큰: {}", token);
            return token;
        }
        // 쿠키에서 토큰 추출
        log.info("헤더에서 토큰이 없으므로 쿠키에서 토큰을 확인합니다.");
        String cookieToken = getTokenFromCookies(request);
        if (cookieToken != null) {
            log.info("쿠키에서 추출된 토큰: {}", cookieToken);
            return cookieToken;
        }

        log.warn("토큰이 헤더와 쿠키 둘 다에 존재하지 않음.");
        return null;
    }
    
    //redis 에 있는 리프레시토큰 가져오는 메서드
    private Optional<String> getRefreshToken(String userid){
        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        String refreshToken = valueOperations.get("refreshToken:"+userid);
        return Optional.ofNullable(refreshToken);
    }

    //리프레시 토큰 비교 메서드
    private boolean isRefreshTokenValid(String userid,String refreshToken){
        Optional<String> storedRefreshToken = getRefreshToken(userid);
        return storedRefreshToken.isPresent() && storedRefreshToken.get().equals(refreshToken);
    }

    public String resolveRefreshToken(HttpServletRequest request){
        String userid = request.getHeader("userid");
        if(userid == null){
            log.info("유저 아이디가 헤더에 없음");
            return null;
        }
        log.info("Redis에서 User-Id를 기반으로 리프레시 토큰 조회 중: {}", userid);

        // Redis에서 리프레시 토큰 조회
        String redisKey = "refreshToken:" + userid;
        String refreshToken = redisTemplate.opsForValue().get(redisKey);

        if(refreshToken == null){
            log.warn("Redis에서 리프레시 토큰을 찾을 수 없습니다. User-Id: {}", userid);
            return null;
        }
        log.info("Redis에서 리프레시 토큰 조회 성공: {}", refreshToken);
        return refreshToken;
    }

}
