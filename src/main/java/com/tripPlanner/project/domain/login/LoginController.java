package com.tripPlanner.project.domain.login;


import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
    public ResponseEntity<LoginDto> login_post(
            @ModelAttribute LoginDto loginDto
    ){
     loginService.findByUser(loginDto); //loginDto 로 유저정보를 조회함
    return new ResponseEntity<>(loginDto, HttpStatus.OK); //Http리스폰과 함꼐 다시 정보를 리턴
    //근데 json 데이터로 전달해야하잖아.
    }


}

