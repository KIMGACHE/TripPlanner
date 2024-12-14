//package com.tripPlanner.project.domain.login;
//
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RequestMapping("/oauth2")
//@RestController
//public class Oauth2Controller {
//
//    @Value("${spring.security.oauth2.client.registration.google.client-id}")
//    private String googleClientId;
//
//    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
//    private String googleClientSecret;
//
//    @GetMapping("/google-client-id")
//    public String getGoogleClientId() {
//        String reqUrl = "";
//        return "Google Client ID: " + googleClientId;
//    }
//}
