package com.tripPlanner.project.domain.login.controller;


import com.tripPlanner.project.domain.login.dto.LoginRequest;
import com.tripPlanner.project.domain.login.dto.LoginResponse;
import com.tripPlanner.project.domain.login.service.AuthService;
import com.tripPlanner.project.domain.login.service.LoginService;
import com.tripPlanner.project.domain.signin.UserEntity;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
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
        List<UserEntity> users = authService.findUserIdByEmail(email);
        
        if(optionalUser.isPresent()){
        //유저 ID 추출
            String userid = optionalUser.get().getUserid();
            return ResponseEntity.ok(Collections.singletonMap("userid",userid));

        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message","유저를 찾을 수 없습니다")); 
        }
    }

    //비밀번호 찾기 요청
    @PostMapping("/password-reset")
    @ResponseBody
    public ResponseEntity<?> findUserPassword(@RequestBody Map<String,String> request){
        String email = request.get("email");
        Optional<UserEntity> optionalUser = authService.findUserIdByEmail(email); //변경

        if(optionalUser.isPresent()){
            String token = authService.generatePasswordResetToken(email);
            String resetLink = "http://localhost:3000/reset-password?token=" + token;
            //이메일 발송 로직
            authService.sendAuthMail(email,"비밀번호 재설정 요청",
                    "비밀번호를 재설정 하려면 다음 요청을 클릭하세요:\n" + resetLink);

            return ResponseEntity.ok(Collections.singletonMap("message","이메일이 발송되었습니다"));
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message","해당 이메일이 존재하지 않습니다"));
        }
    }
    
    //인증 토큰 받은 후 비밀번호 변경
    @PostMapping("/reset-password")
    @ResponseBody
    public ResponseEntity<?> resetPassword(@RequestBody Map<String,String> request){
        String token = request.get("token");
        String newPassword = request.get("newPassword");

        String email = authService.validateToken(token);
        if(email == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message","유효하지 않은 토큰입니다"));
        }

        authService.updatePassword(email,newPassword);
        return ResponseEntity.ok(Collections.singletonMap("message","비밀번호가 성공적으로 변경되었습니다"));
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

