package com.tripPlanner.project.domain.login;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    LoginRepository loginRepository;

    public Map<String, String> findByUser(LoginDto dto){
    Map<String,String> loginData = new HashMap<>();

    LoginEntity.toEntity(dto);
//        Optional<LoginEntity> data = loginRepository.findByLoginInfo(dto);

        loginData.put("userid",dto.getUserid());
        loginData.put("password",dto.getPassword());

        return loginData;
    }
}




