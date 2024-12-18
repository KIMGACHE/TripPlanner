package com.tripPlanner.project.domain.login.service;

import com.tripPlanner.project.domain.login.dto.LoginRequest;
import com.tripPlanner.project.domain.login.dto.LoginResponse;
import com.tripPlanner.project.domain.login.entity.TokenEntity;
import com.tripPlanner.project.domain.login.entity.TokenRepository;
import com.tripPlanner.project.domain.login.entity.UserEntity;
import com.tripPlanner.project.domain.login.entity.UserRepository;
import com.tripPlanner.project.domain.login.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private TokenRepository tokenRepository;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("로그인 서비스 테스트")
    void loginServiceTest(){
        String userid = "qwer";
        String password = "1234";

        UserEntity userEntity = UserEntity.builder()
                .userid(userid)
                .password(password)
                .username("호날두")
                .build();
        LoginRequest loginRequest = LoginRequest.builder()
                .userid(userid)
                .password(password)
                .build();
        when(userRepository.findByUserid(userid)).thenReturn(Optional.of(userEntity));
        when(jwtTokenProvider.generateAccessToken(userid)).thenReturn("accessToken");
        when(jwtTokenProvider.generateRefreshToken(userid)).thenReturn("refreshToken");

        // When
        LoginResponse response = loginService.login(loginRequest);

        // Then
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("로그인 성공", response.getMessage());
        assertEquals(userid, response.getUserid());
        verify(userRepository, times(1)).findByUserid(userid);
        verify(jwtTokenProvider, times(1)).generateAccessToken(userid);
        verify(jwtTokenProvider, times(1)).generateRefreshToken(userid);
        verify(tokenRepository, times(1)).save(any(TokenEntity.class));

    }


}