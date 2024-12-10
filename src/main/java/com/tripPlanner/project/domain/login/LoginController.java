package com.tripPlanner.project.domain.login;


import com.tripPlanner.project.domain.user.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Controller
public class LoginController {

    @Autowired
    LoginService loginService;

//    @Autowired
//    LoginRepository loginRepository;

    @GetMapping("/login")
    public void login(){
    log.info("LOGIN PAGE GET");
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<UserEntity> login_post(
            @RequestBody LoginRequest loginRequest
    ){
      UserEntity entity = loginService.login(loginRequest); //loginDto 로 유저정보를 조회함
    return ResponseEntity.ok(entity); //Json 데이터로 전달
    }

}

