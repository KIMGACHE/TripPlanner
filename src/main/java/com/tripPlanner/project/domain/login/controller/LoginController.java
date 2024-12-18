package com.tripPlanner.project.domain.login.controller;


import com.tripPlanner.project.domain.login.dto.LoginRequest;
import com.tripPlanner.project.domain.login.dto.LoginResponse;
import com.tripPlanner.project.domain.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

    @PostMapping(value="/login",consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<LoginResponse> login_post(
            @RequestBody LoginRequest loginRequest
    ){
        log.info("login post mapping" + loginRequest);
      LoginResponse response = loginService.login(loginRequest); //loginDto 로 유저정보를 조회함
        log.info("반갑습니다");
        System.out.println(response);
        if(!response.isSuccess()){
            return ResponseEntity.badRequest().body(response);
        }
    return ResponseEntity.ok(response); //Json 데이터로 전달
    }

}

