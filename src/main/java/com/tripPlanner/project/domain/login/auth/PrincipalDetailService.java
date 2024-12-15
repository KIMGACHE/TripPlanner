package com.tripPlanner.project.domain.login.auth;

import com.tripPlanner.project.domain.login.LoginRequest;
import com.tripPlanner.project.domain.user.UserEntity;
import com.tripPlanner.project.domain.login.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalDetailService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("UserDetailService..." );
        UserEntity userEntity = userRepository.findByUserid(username)
                .orElseThrow(()-> new UsernameNotFoundException("유저 정보를 찾을 수 없습니다 : " + username));

        LoginRequest loginRequest = LoginRequest.builder()
                .userid(userEntity.getUserid())
                .password(userEntity.getPassword())
                .build();

        return new PrincipalDetail(loginRequest);
    }
}






