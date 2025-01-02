package com.tripPlanner.project.domain.mypage.controller;

import com.tripPlanner.project.domain.login.auth.jwt.JwtTokenProvider;
import com.tripPlanner.project.domain.mypage.Service.MypageService;
import com.tripPlanner.project.domain.mypage.UpdateUserRequest;
import com.tripPlanner.project.domain.signin.entity.UserEntity;
import com.tripPlanner.project.domain.signin.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
@RequestMapping("/user/mypage")
@RequiredArgsConstructor
@Slf4j
public class MypageController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MypageService mypageService;

    private final String UPLOAD_DIR = "C:/upload/profile/";

    // 쿠키에서 accessToken을 읽어오는 메소드
    private String getAccessTokenFromCookies(HttpServletRequest request) {
        String accessToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    accessToken = cookie.getValue();
                    break;
                }
            }
        }
        return accessToken;
    }

    // 이미지 반환 메소드
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/upload/profile/{userid}/{fileName}")
    public ResponseEntity<Resource> getImage(@PathVariable String userid, @PathVariable String fileName) {
        log.info("GET 이미지 요청: userid={}, fileName={}", userid, fileName);
        Path path = Paths.get(UPLOAD_DIR + userid + "/" + fileName);
        Resource resource = new FileSystemResource(path);

        if (!resource.exists()) {
            log.error("이미지가 존재하지 않습니다: {}", path);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        String contentType = "application/octet-stream";
        try {
            contentType = Files.probeContentType(path);
        } catch (IOException e) {
            log.error("파일 유형을 결정할 수 없습니다.", e);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    // 사용자 정보를 반환하는 메소드
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping
    public ResponseEntity<?> getUserData(HttpServletRequest request) {
        log.info("GET /user/mypage - Fetching user data...");

        String accessToken = getAccessTokenFromCookies(request);
        if (accessToken == null) {
            log.warn("엑세스 토큰이 쿠키에 없습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("엑세스 토큰이 쿠키에 없습니다.");
        }

        if (!jwtTokenProvider.validateToken(accessToken)) {
            log.warn("유효하지 않은 토큰입니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
        }

        String userid = jwtTokenProvider.getUserIdFromToken(accessToken);
        Optional<UserEntity> optionalUser = userRepository.findByUserid(userid);

        if (optionalUser.isEmpty()) {
            log.warn("유저가 존재하지 않습니다: {}", userid);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저를 찾을 수 없습니다.");
        }

        UserEntity user = optionalUser.get();
        log.info("사용자 정보: {}", user);
        return ResponseEntity.ok(user);
    }



    // 사용자 정보 업데이트
    @PutMapping("/userupdate")
    public ResponseEntity<?> updateUserInfo(HttpServletRequest request, @RequestBody UpdateUserRequest userRequest) {
        log.info("PUT /user/mypage/userupdate - 사용자 정보 업데이트");

        String accessToken = getAccessTokenFromCookies(request);
        if (accessToken == null || !jwtTokenProvider.validateToken(accessToken)) {
            log.warn("유효하지 않은 엑세스 토큰.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 엑세스 토큰입니다.");
        }

        String userid = jwtTokenProvider.getUserIdFromToken(accessToken);
        Optional<UserEntity> optionalUser = userRepository.findByUserid(userid);
        if (optionalUser.isEmpty()) {
            log.warn("유저를 찾을 수 없습니다: {}", userid);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저를 찾을 수 없습니다.");
        }

        try {
            // 유효성 검사
            mypageService.validateUpdateRequest(userRequest);

            UserEntity user = optionalUser.get();
            if (userRequest.getUsername() != null) {
                user.setUsername(userRequest.getUsername());
            }
            if (userRequest.getImg() != null) {
                user.setImg(userRequest.getImg());
            }
            if (userRequest.getEmail() != null) {
                user.setEmail(userRequest.getEmail());
            }
            if (userRequest.getPassword() != null && userRequest.getPassword().equals(userRequest.getRepassword())) {
                user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            } else if (userRequest.getPassword() != null) {
                throw new IllegalArgumentException("비밀번호 확인이 일치하지 않습니다.");
            }

            userRepository.save(user);
            log.info("유저 정보 업데이트 성공: {}", user);
            return ResponseEntity.ok("유저 정보가 성공적으로 업데이트되었습니다.");
        } catch (IllegalArgumentException e) {
            log.warn("유효성 검사 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 이미지 업로드 메서드
    @PostMapping("/upload")
    public ResponseEntity<?> uploadProfileImage(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        log.info("POST /user/mypage/upload - 프로필 이미지 업로드 요청");

        String accessToken = getAccessTokenFromCookies(request);
        if (accessToken == null || !jwtTokenProvider.validateToken(accessToken)) {
            log.warn("유효하지 않은 엑세스 토큰.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 엑세스 토큰입니다.");
        }

        String userid = jwtTokenProvider.getUserIdFromToken(accessToken);
        Optional<UserEntity> optionalUser = userRepository.findByUserid(userid);
        if (optionalUser.isEmpty()) {
            log.warn("유저를 찾을 수 없습니다: {}", userid);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저를 찾을 수 없습니다.");
        }

        // 업로드된 파일이 비어있는지 확인
        if (file.isEmpty()) {
            log.warn("업로드된 파일이 없습니다. 기본 이미지 사용.");

            // 기본 이미지 경로 설정
            Path defaultImagePath = Paths.get("C:/upload/profile/default.jpg");

            // 유저별 디렉토리 생성
            Path userDir = Paths.get(UPLOAD_DIR, userid);
            File userDirFile = userDir.toFile();
            if (!userDirFile.exists()) {
                boolean dirCreated = userDirFile.mkdirs();
                if (!dirCreated) {
                    log.error("디렉토리 생성 실패: {}", userDir);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("디렉토리 생성에 실패했습니다.");
                }
            }

            try {
                // 기본 이미지를 유저 디렉토리로 복사
                Path filePath = userDir.resolve("profile.jpg");
                Files.copy(defaultImagePath, filePath);

                // 유저 엔티티 업데이트
                UserEntity user = optionalUser.get();
                user.setImg("/upload/profile/" + userid + "/profile.jpg");
                userRepository.save(user);

                log.info("기본 이미지 업로드 성공: {}", filePath);
                return ResponseEntity.ok("기본 이미지 업로드 성공");
            } catch (IOException e) {
                log.error("기본 이미지 업로드 실패: {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("기본 이미지 업로드 중 오류가 발생했습니다.");
            }
        }

        try {
            // 유저별 디렉토리 생성
            Path userDir = Paths.get(UPLOAD_DIR, userid);
            File userDirFile = userDir.toFile();
            if (!userDirFile.exists()) {
                boolean dirCreated = userDirFile.mkdirs();
                if (!dirCreated) {
                    log.error("디렉토리 생성 실패: {}", userDir);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("디렉토리 생성에 실패했습니다.");
                }
            }

            // 파일 저장
            String fileName = file.getOriginalFilename();
            Path filePath = userDir.resolve(fileName);
            file.transferTo(filePath.toFile());

            // 유저 엔티티 업데이트
            UserEntity user = optionalUser.get();
            user.setImg("/upload/profile/" + userid + "/" + fileName);
            userRepository.save(user);

            log.info("파일 업로드 성공: {}", filePath);
            return ResponseEntity.ok("파일 업로드 성공");
        } catch (IOException e) {
            log.error("파일 업로드 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 중 오류가 발생했습니다.");
        }
    }
}
