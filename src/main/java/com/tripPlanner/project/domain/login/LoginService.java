package com.tripPlanner.project.domain.login;

import com.tripPlanner.project.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class LoginService {


    private LoginRepository loginRepository;

    private User user;

    public Map<String, String> findByUser(LoginDto dto){
    Map<String,String> loginData = new HashMap<>();

    Optional<LoginEntity> optionalEntity = loginRepository.findByUserid(dto.getUserid());

    if(optionalEntity.isPresent()){  //User 정보가 존재하는지 확인
        LoginEntity entity = optionalEntity.get();
        loginData.put("userid",dto.getUserid());
        loginData.put("password",dto.getPassword());
    }else{
        throw new RuntimeException("User not Found"); //User 정보가 없다면 예외 발생
    }


        return loginData;
    }
    
    
public void loginValid(UserDto userDto){
        String userid = userDto.
}


}




