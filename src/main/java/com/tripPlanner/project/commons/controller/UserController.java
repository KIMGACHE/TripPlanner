package com.tripPlanner.project.commons.controller;

import com.tripPlanner.project.domain.signin.UploadProperties;
import com.tripPlanner.project.domain.signin.UserDto;
import com.tripPlanner.project.domain.signin.UserEntity;
import com.tripPlanner.project.domain.signin.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Controller
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/join")
    public void join() {
        log.info("Get /join...");
    }

    @PostMapping("/join")
    public String join_post(UserDto userDto, @RequestParam("profileImage") MultipartFile profileImage) throws IOException {
        log.info("Post /join " + userDto);

        // 비밀번호가 null인지 확인
        if (userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
            log.error("Password cannot be null or empty");
            return "redirect:/user/join?error=password";
        }

        UploadProperties uploadProperties = new UploadProperties();

        String dirPath =
                UploadProperties.uploadPath
                        + File.separator
                        + UploadProperties.profilePath
                        + File.separator
                        + userDto.getUserid();
        log.info("Directory Path: {}", dirPath);

        // 디렉토리 생성
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 업로드된 프로필 이미지가 있는 경우 해당 이미지 파일 저장
        if (!profileImage.isEmpty()) {
            File profileFile = new File(dirPath + File.separator + profileImage.getOriginalFilename());
            profileImage.transferTo(profileFile);
            log.info("Profile image saved: {}", profileFile.getAbsolutePath());
        } else {
            // 기본 이미지 복사 처리
            String defaultImagePath = "src/main/resources/static/assets/anonymous.jpg";
            File defaultImage = new File(defaultImagePath);
            File copyDefaultImage = new File(dirPath + File.separator + "profileImg.jpg");
            if (defaultImage.exists()) {
                Files.copy(defaultImage.toPath(), copyDefaultImage.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            log.info("Using default profile image: {}", copyDefaultImage.getAbsolutePath());
        }

        // 사용자 정보를 저장
        UserEntity user = UserEntity.builder()
                .userid(userDto.getUserid())
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword())) // 암호화
                .email(userDto.getEmail())
                .addr(userDto.getAddr())
                .birth(userDto.getBirth())
                .img(profileImage.isEmpty() ? "profileImg.jpg" : profileImage.getOriginalFilename())
                .build();

        userRepository.save(user);
        log.info("User saved successfully: {}", user);

        return "redirect:/"; // 홈 화면으로 리다이렉트
    }
}
