package com.tripPlanner.project.domain.login.controller;


import com.tripPlanner.project.domain.login.auth.handler.CustomLogoutHandler;
import com.tripPlanner.project.domain.login.dto.LoginRequest;
import com.tripPlanner.project.domain.login.dto.LoginResponse;
import com.tripPlanner.project.domain.login.service.AuthService;
import com.tripPlanner.project.domain.login.service.LoginService;
import com.tripPlanner.project.domain.signin.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000") //리액트 도메인 허가
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
    
    //아이디 찾기
    @PostMapping(value="/findId")
    @ResponseBody
    public ResponseEntity<?> findUserid(@RequestBody Map<String,String> request){
        String email = request.get("email");
        String userid = loginService.findUserByEmail(email);

        if(userid != null) {
            return ResponseEntity.ok(Collections.singletonMap("userid",userid));
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message","유저를 찾을 수 없습니다"));
        }
    }

    @PostMapping("/password-reset")
    @ResponseBody
    public ResponseEntity<?> findUserPassword(@RequestBody Map<String,String> request){
        String email = request.get("email");
        Optional<UserEntity> optionalUser = loginService.findUserByEmail(email);

        if(optionalUser.isPresent()){
            String token =
        }


    }


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

