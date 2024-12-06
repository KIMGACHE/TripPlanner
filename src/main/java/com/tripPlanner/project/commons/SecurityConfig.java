package com.tripPlanner.project.commons;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // csrf 비활성화
        http.csrf(AbstractHttpConfigurer::disable);

        // httpBasic 비활성화
        http.httpBasic(AbstractHttpConfigurer::disable);

        // 폼로그인 비활성화 (jwt사용하기 위해)
        http.formLogin(AbstractHttpConfigurer::disable);

        // 정적 경로
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/*/*","/*").permitAll() // 인증 없이 허용할 경로
//                .requestMatchers("").denyAll() // 인증 없으면 허용하지 않을 경로
                .anyRequest().authenticated());

        // 로그아웃
//        http.logout((logout) -> logout.logoutSuccessUrl("/").invalidateHttpSession(true));


        // 소셜 로그인 (입맛에 맞춰 쓰면 됩니다)
//        http.oauth2Login(oauth2 -> {
//            oauth2.loginPage("/login").userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
//                    .successHandler(oAuth2SuccessHandler).failureHandler(oAuth2ErrorHandler);
//        });


        // 세션 비활성화
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        return http.build();
    }

    // 비밀번호 암호화
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 정적 자원 경로 허용
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//            return (web) -> web.ignoring().requestMatchers("/favicon.ico");
//    }


}