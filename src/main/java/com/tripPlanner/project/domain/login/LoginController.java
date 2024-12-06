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

//@Controller
@Slf4j
@RestController
public class LoginController {

    @Autowired
    LoginService loginService;

    @Autowired
    LoginRepository loginRepository;

    @GetMapping("/login")
    public void login(){

    log.info("LOGIN PAGE GET");

//        loginService.userLogin(loginDto);

//        model.addAttribute("model",model);
    }

    @PostMapping("/login")
    public ResponseEntity<> login_post(
            @RequestBody LoginDto loginDto
    ){



     loginService.findByUser(loginDto);
    return new ResponseEntity<>(, HttpStatus.OK);

    }


}

