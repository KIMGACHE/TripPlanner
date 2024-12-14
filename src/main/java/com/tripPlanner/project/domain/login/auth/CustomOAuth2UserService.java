package com.tripPlanner.project.domain.login.auth;

import com.tripPlanner.project.domain.login.UserRepository;
import com.tripPlanner.project.domain.user.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        Optional<UserEntity> optionalUser = userRepository.findByUserid(userid);
        UserEntity userEntity;

        if(optionalUser.isEmpty()){ //로그인 할 때 사용자 정보가 없다면 그대로 회원가입이 되도록 진행
            userEntity = createUserEntity(userid,oAuth2User,provider, providerId);
            userRepository.save(userEntity);
        }else{
            userEntity = optionalUser.get();
        }

        // UserDetails 객체로 반환
        return new PrincipalDetail(userEntity,oAuth2User.getAttributes());
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
    private UserEntity createUserEntity(String userid, OAuth2User oAuth2User, String provider, String providerId){
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
                username = (String) ((Map<String,Object>) ((Map<String,Object>) oAuth2User.getAttribute("kakao_acount")).get("profile")).get("nickname");
                email = (String) ((Map<String,Object>) oAuth2User.getAttribute("kakao_acount")).get("email");
                break;
            case "instagram" :
                username = ""; //아직 미정
                email = "";
                break;
            default :
                throw new IllegalArgumentException("사용하지 않는 제공자입니다");
        }
        return UserEntity.builder()
                .userid(userid)
                .username(username)
                .email(email)
                .provider(provider)
                .providerId(providerId)
                .build();
    }

}
