package com.tripPlanner.project.domain.login.auth;

import com.tripPlanner.project.domain.user.UserEntity;
import com.tripPlanner.project.domain.login.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrincipalDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

//        Optional<UserEntity> userOptional = this.userRepository.findById(username);
//        if(userOptional.isEmpty()){ //user 정보가 없을 경우
//            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다");
//        }
//        UserEntity userEntity = userOptional.get(); //user 정보 가져오기


        return null;
    }
}






