package com.tripPlanner.project.domain.login.service;


import com.tripPlanner.project.domain.login.auth.PrincipalDetail;
import com.tripPlanner.project.domain.login.dto.JwtToken;
import com.tripPlanner.project.domain.login.dto.LoginRequest;
import com.tripPlanner.project.domain.login.dto.LoginResponse;
import com.tripPlanner.project.domain.login.entity.TokenEntity;
import com.tripPlanner.project.domain.login.entity.TokenRepository;
import com.tripPlanner.project.domain.login.entity.UserRepository;
import com.tripPlanner.project.domain.login.entity.UserEntity;
import com.tripPlanner.project.domain.login.jwt.JwtTokenProvider;
import io.jsonwebtoken.Jwt;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LoginService {
    //비밀번호 정규 표현식. 하나 이상의 영어 대문자 , 하나 이상의 특수기호 , 하나 이상의 숫자 , 8글자 13글자 사이
   //private static final String USERPW_RegExp = "/^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,13}/";


    private final UserRepository userRepository;
//  private final BCryptPasswordEncoder passwordEncoder;  //비밀번호 암호화
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;

    public LoginResponse login(LoginRequest loginRequest){ //로그인 기능
        log.info("로그인 서비스 함수 실행");
        Optional<UserEntity> optionalUser = userRepository.findByUserid(loginRequest.getUserid());
        emptyCheckUserIdAndPassword(loginRequest.getUserid(),loginRequest.getPassword());
        if(optionalUser.isEmpty()){ //loginId와 일치하는 user없으면 에러 리턴
            return LoginResponse.builder()
                    .success(false)
                    .message("유저를 찾을 수 없습니다 !")
                    .build();
        }

        UserEntity userEntity = optionalUser.get(); 
        
        if(!userEntity.getPassword().equals(loginRequest.getPassword())){ //User의 password와 입력 password가 다르면 에러 리턴
            return LoginResponse.builder()
                    .success(false)
                    .message("비밀번호가 일치하지 않습니다")
                    .build();
        }
        //UserDetail 생성
        PrincipalDetail principalDetail = new PrincipalDetail(
                LoginRequest.builder()
                        .userid(userEntity.getUserid())
                        .password(userEntity.getPassword())
                        .build()
        );

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(principalDetail,null,principalDetail.getAuthorities());
    
        log.info("시큐리티 인증정보 저장완료" + SecurityContextHolder.getContext().getAuthentication()); //ROLE USER 가 들어가지 않는다 !
        
        //JWT 토큰 생성
        String accessToken = jwtTokenProvider.generateAccessToken(userEntity.getUserid());
        String refreshToken = jwtTokenProvider.generateRefreshToken(userEntity.getUserid());

        TokenEntity tokenEntity = new TokenEntity(userEntity.getUserid(),refreshToken);
        tokenRepository.save(tokenEntity);
        log.info("토큰엔티티"+tokenEntity);

        return LoginResponse.builder()
                .userid(userEntity.getUserid())
                .username(userEntity.getUsername())
                .success(true)
                .message("로그인 성공")
                .build();
    }

    //빈칸 검사 함수
    private void emptyCheckUserIdAndPassword(String userid,String userpw){
        log.info("빈칸 검사 실행");
        if(userid == null || userid.trim().isEmpty()){
            throw new IllegalArgumentException("아이디를 입력해주세요");
        }

        if(userpw==null || userpw.trim().isEmpty()){
            throw new IllegalArgumentException("비밀번호를 입력해주세요");
        }

        else{
            log.info("값이 입력되어있습니다.");
        }
    }



}




