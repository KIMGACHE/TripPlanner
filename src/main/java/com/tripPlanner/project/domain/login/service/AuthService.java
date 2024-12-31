package com.tripPlanner.project.domain.login.service;

import com.tripPlanner.project.domain.login.auth.jwt.JwtTokenProvider;
import com.tripPlanner.project.domain.login.dto.LoginResponse;
import com.tripPlanner.project.domain.signin.entity.UserEntity;
import com.tripPlanner.project.domain.signin.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String,Object> redisTemplate;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;

    private String getKey(){
        String secretKey = jwtTokenProvider.getSecretKey();
        return  secretKey;
    }

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
    public void setTokenCookies(HttpServletResponse response, String accessToken) {
        ResponseCookie cookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(true) // HTTPS 사용 시 true로 변경
                .sameSite("None") // CORS 요청에서도 쿠키 허용
                .path("/")
                .maxAge(30 * 60) // 30분 유효
                .build();

        log.info("쿠키쿠키 : {}",cookie);
        response.addHeader("Set-Cookie", cookie.toString());
    }
    
    //이메일 인증 메서드
    public void sendAuthMail(String to,String subject,String body){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("wlscksals@gmail.com");

        javaMailSender.send(message);

    }

    //비밀번호 변경 토큰 발급 메서드
    public String generatePasswordResetToken(String email){
        long expiration = 1000 * 60 * 5; //5분
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(SignatureAlgorithm.HS256,getKey())
                .compact();
    }

    //비밀번호 찾기 토큰 유효성검사
    public String validateToken(String token){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();  //이메일 반환
        }catch(Exception e){
            return null; //유효하지 않은 토큰
        }
    }

    //비밀번호 변경 저장 메서드
    public void updatePassword(String email,String newPassword){
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));
        userEntity.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userEntity);
    }

//    public List<UserEntity> findUserIdByEmail(String email){
//        return userRepository.findAllByEmail(email);
//    }



}
