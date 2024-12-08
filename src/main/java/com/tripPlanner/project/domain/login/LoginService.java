package com.tripPlanner.project.domain.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    LoginRepository loginRepository;

    public Map<String, String> findByUser(LoginDto dto){
    Map<String,String> loginData = new HashMap<>();

    LoginEntity entity= LoginEntity.toEntity(dto);
        //이건 로그인 데이터를 반환하는거고 넣는거는 엔티티로 만들어서 넣어야함
        loginData.put("userid",dto.getUserid());
        loginData.put("password",dto.getPassword());
        loginRepository.save(entity);
        return loginData;
    }
}




