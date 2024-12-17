package com.tripPlanner.project.domain.login.service;

import com.tripPlanner.project.domain.login.auth.PrincipalDetail;
import com.tripPlanner.project.domain.login.dto.LoginRequest;
import com.tripPlanner.project.domain.login.entity.UserRepository;
import com.tripPlanner.project.domain.login.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);  // oauth2
        log.info("getAttributes : " + oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId(); //provider 값 (naver, google 등)
        String providerId = getProviderId(oAuth2User,provider);
        String userid = provider + "_" + providerId;

        log.info("userid 는 ? " + userid);

        Optional<UserEntity> optionalUser = userRepository.findByUserid(userid);
        UserEntity userEntity;

        LoginRequest loginRequest = null;
        if(optionalUser.isEmpty()){ //로그인 할 때 사용자 정보가 없다면 그대로 회원가입이 되도록 진행
            userEntity = createUserEntity(userid,oAuth2User,"ROLE_USER",provider, providerId);
            userRepository.save(userEntity);

            loginRequest = LoginRequest.builder()
                    .userid(userid)
                    .password(userEntity.getPassword()) //비밀번호는 인코딩 된걸로 사용하게 변경
                    .provider(provider)
                    .providerId(providerId)
                    .build();
        }else{
            userEntity = optionalUser.get();
             loginRequest = LoginRequest.builder()
                    .userid(userid)
                    .password(userEntity.getPassword())
                    .provider(provider)
                    .providerId(providerId)
                    .build();
        }

        // UserDetails 객체로 반환
        PrincipalDetail principalDetail = new PrincipalDetail();
        principalDetail.setLoginRequest(loginRequest);
        principalDetail.setAttributes(oAuth2User.getAttributes());

        //spring security Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(principalDetail,null, principalDetail.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken); //시큐리티 컨텍스트에 저장
        return principalDetail;
    }

    //provider 에 따라 providerId를 다르게 처리
    private String getProviderId(OAuth2User oAuth2User, String provider){
        switch(provider){
            case "google" :
                return oAuth2User.getAttribute("sub");
            case "naver" :
                return (String) ((Map<String,Object>) oAuth2User.getAttribute("response")).get("id");
            case "kakao" :
                return oAuth2User.getAttribute("id").toString();
            case "instagram" :
                return oAuth2User.getAttribute("id");
            default :
                throw new IllegalArgumentException("지원하지 않는 제공자입니다.");
        }
    }

    //UserEntity 만드는 함수
    private UserEntity createUserEntity(String userid, OAuth2User oAuth2User, String role,String provider, String providerId){
        String username;
        String email;

        switch(provider){
            case "google" :
                username = oAuth2User.getAttribute("name");
                email = oAuth2User.getAttribute("email");
                break;
            case "naver" :
                username = (String) ((Map<String,Object>) oAuth2User.getAttribute("response")).get("nickname");
                email = (String) ((Map<String,Object>) oAuth2User.getAttribute("response")).get("email");
                break;
            case "kakao" :
                Map<String, Object> properties = oAuth2User.getAttribute("properties");
                username = (String) properties.get("nickname");
                Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
                email = (String) kakaoAccount.get("email");
                break;
            case "instagram" :
                username = ""; //아직 미구현
                email = "";
                break;
            default :
                throw new IllegalArgumentException("사용하지 않는 제공자입니다");
        }
        return UserEntity.builder()
                .userid(userid)
                .username(username)
                .email(email)
                .role("ROLE_USER")
                .provider(provider)
                .providerId(providerId)
                .build();
    }

}
