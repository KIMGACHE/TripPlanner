package com.tripPlanner.project.domain.login.controller;


import com.tripPlanner.project.domain.login.dto.LoginRequest;
import com.tripPlanner.project.domain.login.dto.LoginResponse;
import com.tripPlanner.project.domain.login.service.AuthService;
import com.tripPlanner.project.domain.login.service.LoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final AuthService authService;

    @GetMapping("/login")
    public String login(){
    log.info("LOGIN PAGE GET mapping");
    return "login";
    }

    @PostMapping(value="/login")
    @ResponseBody
    public ResponseEntity<LoginResponse> login_post(
            @RequestBody LoginRequest loginRequest,
            HttpServletResponse servletResponse
    ){
        log.info("login post mapping" + loginRequest);
      LoginResponse response = loginService.login(loginRequest); //loginDto 로 유저정보를 조회함
        System.out.println(response);
        if(!response.isSuccess()){
            return ResponseEntity.badRequest().body(response);
        }
        authService.setTokenCookies(servletResponse, response.getAccessToken());

    return ResponseEntity.ok(response); //Json 데이터로 전달
    }
    
//    //로그아웃 메서드
//    @PostMapping(value="/logout")
//    public ResponseEntity<Void> logout(){
//
//        return ResponseEntity.ok().build();
//
//    }


//    @PostMapping("/refresh")
//    @ResponseBody
//    public ResponseEntity<LoginResponse> refreshAccessToken(
//        @RequestBody Map<String ,String> request,
//        HttpServletResponse response
//    ){
//        String accessToken = request.get("accessToken");
//        String refreshToken = request.get("refreshToken");
//
//        //AuthService 호출
//        LoginResponse loginResponse = authService.refreshAccessToken(accessToken,refreshToken);
//
//        //실패 시 바로 응답
//        if(!loginResponse.isSuccess()){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponse);
//        }
//
//        //새로운 엑세스 토큰 쿠키 저장
//        authService.setTokenCookies(response,accessToken);
//
//        return ResponseEntity.ok(loginResponse);
//    }



}

