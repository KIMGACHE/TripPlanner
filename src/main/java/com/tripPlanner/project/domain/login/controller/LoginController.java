package com.tripPlanner.project.domain.login.controller;


import com.tripPlanner.project.domain.login.dto.LoginRequest;
import com.tripPlanner.project.domain.login.dto.LoginResponse;
import com.tripPlanner.project.domain.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {


    private final LoginService loginService;

//    @Autowired
//    LoginRepository loginRepository;

    @GetMapping("/login")
    public String login(){
    log.info("LOGIN PAGE GET mapping");
    return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<LoginResponse> login_post(
            @RequestBody LoginRequest loginRequest
    ){
        log.info("login post mapping");
      LoginResponse response = loginService.login(loginRequest); //loginDto 로 유저정보를 조회함
        if(!response.isSuccess()){
            return ResponseEntity.badRequest().body(response);
        }
    return ResponseEntity.ok(response); //Json 데이터로 전달
    }

}

