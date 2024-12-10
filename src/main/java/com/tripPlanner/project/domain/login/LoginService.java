package com.tripPlanner.project.domain.login;

import com.tripPlanner.project.domain.user.UserEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LoginService {
    //비밀번호 정규 표현식. 하나 이상의 영어 대문자 , 하나 이상의 특수기호 , 하나 이상의 숫자 , 8글자 13글자 사이
   //private static final String USERPW_RegExp = "/^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,13}/";


    private final UserRepository userRepository;
//  private final BCryptPasswordEncoder passwordEncoder;  //비밀번호 암호화

    public boolean checkLoginIdDuplicate(String userid){ //아이디 중복 체크
        return userRepository.existsByUserid(userid);
    }

    public boolean checkUsernameDuplicate(String username){ //닉네임 중복 체크
        return userRepository.existsByUsername(username);
    }

    public UserEntity login(LoginRequest loginRequest){ //로그인 기능
        Optional<UserEntity> optionalUser = userRepository.findByUserid(loginRequest.getUserid());
        emptyCheckUserIdAndPassword(loginRequest.getUserid(),loginRequest.getPassword());
        if(optionalUser.isEmpty()){ //loginId와 일치하는 user없으면 null 리턴
            return null;
        }

        UserEntity userEntity = optionalUser.get(); 
        
        if(userEntity.getPassword().equals(loginRequest.getPassword())){ //User의 password와 입력 password가 다르면 null 리턴
            return null;
        }
        return userEntity;
    }

    private void emptyCheckUserIdAndPassword(String userid,String userpw){ //빈칸 검사
        if(userid == null || userid.trim().isEmpty()){
            throw new IllegalArgumentException("아이디를 입력해주세요");
        }

        if(userpw==null || userpw.trim().isEmpty()){
            throw new IllegalArgumentException("비밀번호를 입력해주세요");
        }


    }







}




