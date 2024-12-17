package com.tripPlanner.project.config;

import com.tripPlanner.project.domain.login.jwt.JwtTokenProvider;
import com.tripPlanner.project.domain.login.service.PrincipalDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
//    private final PrincipalDetailService principalDetailService;

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
                .requestMatchers("/**").permitAll() // 인증 없이 허용할 경로
//                .requestMatchers("").denyAll() // 인증 없으면 허용하지 않을 경로
                .anyRequest().authenticated());

        // 로그아웃
//        http.logout((logout) -> logout.logoutSuccessUrl("/").invalidateHttpSession(true));

//         //소셜 로그인 (입맛에 맞춰 쓰면 됩니다)
        http.oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .defaultSuccessUrl("/") //로그인 성공시 리다이렉트 경로
                .failureUrl("/login?error=true")
                );

        //JWT 인증 필터 추가
//        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        // 세션 비활성화
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        return http.build();
    }

//    @Bean
//    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(principalDetailService);
//    }

    // 비밀번호 암호화
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


//    @Bean   //정적 자원 허용 경로
//    public WebSecurityCustomizer webSecurityCustomizer() { // security를 적용하지 않을 리소스
//        return web -> web.ignoring()
//                // error endpoint를 열어줘야 함, favicon.ico 추가!
//                .requestMatchers("/error", "/favicon.ico","/static/**");
//    }


}