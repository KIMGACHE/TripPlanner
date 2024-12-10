package com.tripPlanner.project.domain.login.auth;

import com.tripPlanner.project.domain.User;
import com.tripPlanner.project.domain.login.LoginRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class PrincipalDetailService implements UserDetailsService {

    private final LoginRepository loginRepository;

    public PrincipalDetailService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userOptional = loginRepository.findByid(username);
        if(userOptional.isEmpty())
            throw new UsernameNotFoundException("유저가 존재하지 않습니다");


        return null;
    }
}






