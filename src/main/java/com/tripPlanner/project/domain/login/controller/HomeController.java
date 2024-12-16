package com.tripPlanner.project.domain.login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // home 경로 맵핑
    @GetMapping("/")
    public String home() {
        return "index";
    }
    
}
