package com.tripPlanner.project.domain.Mypage;

import com.tripPlanner.project.domain.login.auth.jwt.JwtTokenProvider;
import com.tripPlanner.project.domain.signin.UserDto;
import com.tripPlanner.project.domain.signin.UserEntity;
import com.tripPlanner.project.domain.signin.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@RestController
@RequestMapping("/user/mypage")
@RequiredArgsConstructor
@Slf4j
public class MypageController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Value("${file.upload-dir}")
    private String uploadDir; // C:/upload/profile/

    // 사용자 데이터 조회
    @GetMapping
    public ResponseEntity<?> getUserData(HttpServletRequest request) {
        log.info("GET /user/mypage - Fetching user data...");

        String accessToken = getAccessTokenFromCookies(request);
        if (accessToken == null) {
            log.warn("Access token not found in cookies.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access token is missing.");
        }

        if (!jwtTokenProvider.validateToken(accessToken)) {
            log.warn("Invalid or expired token.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token.");
        }

        String userid = jwtTokenProvider.getUserIdFromToken(accessToken);
        Optional<UserEntity> optionalUser = userRepository.findByUserid(userid);

        if (optionalUser.isEmpty()) {
            log.warn("User not found for ID: {}", userid);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        UserEntity user = optionalUser.get();
        String imagePath = user.getImg() != null ? "/upload/profile/" + userid + "/" + user.getImg() : null;

        UserDto userDto = new UserDto(
                user.getUserid(),
                imagePath,
                user.getUsername(),
                null,
                null,
                user.getEmail(),
                user.getBirth(),
                user.getGender(),
                user.getRole()
        );

        log.info("User data successfully retrieved for ID: {}", userid);
        return ResponseEntity.ok(userDto);
    }

    private String getAccessTokenFromCookies(HttpServletRequest request) {
        return Optional.ofNullable(request.getCookies())
                .flatMap(cookies -> {
                    for (Cookie cookie : cookies) {
                        if ("accessToken".equals(cookie.getName())) {
                            return Optional.of(cookie.getValue());
                        }
                    }
                    return Optional.empty();
                })
                .orElse(null);
    }

    // 프로필 이미지 업로드
    @PostMapping("/upload/profile/{userid}")
    public ResponseEntity<String> uploadProfileImage(@PathVariable String userid, @RequestParam("file") MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            String filePath = uploadDir + userid + "/" + fileName;

            File dir = new File(uploadDir + userid);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            Path path = Paths.get(filePath);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            Optional<UserEntity> optionalUser = userRepository.findByUserid(userid);
            if (optionalUser.isPresent()) {
                UserEntity user = optionalUser.get();
                user.setImg(fileName);
                userRepository.save(user);
            }

            log.info("File uploaded successfully: {}", fileName);
            return ResponseEntity.ok("Profile image uploaded successfully!");
        } catch (IOException e) {
            log.error("File upload failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
        }
    }

    // 프로필 이미지 조회
//    @GetMapping("/upload/profile/{userid}/{fileName}")
//    public ResponseEntity<Resource> getImage(@PathVariable String userid, @PathVariable String fileName) {
//        String filePath = uploadDir + userid + "/" + fileName;
//        Path path = Paths.get(filePath);
//        Resource resource = new FileSystemResource(path);
//
//        if (!resource.exists()) {
//            log.warn("File not found: {}", filePath);
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//
//        String contentType = "application/octet-stream";
//        try {
//            contentType = Files.probeContentType(path);
//        } catch (IOException e) {
//            log.error("Failed to determine file type", e);
//        }
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(contentType))
//                .body(resource);
//    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/upload/profile/{userid}/{fileName}")
    public ResponseEntity<Resource> getImage(@PathVariable String userid, @PathVariable String fileName) {
        log.info("GET 이미지 요청: " + fileName);

        // 실제 파일 경로 찾기
        Path path = Paths.get("C:/upload/profile/" + userid + "/" + fileName);
        System.out.println("path 확인 " + path);
        Resource resource = new FileSystemResource(path);

        // 파일이 존재하지 않으면 404 반환
        if (!resource.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // 파일 타입 확인
        String contentType = "application/octet-stream"; // 기본값 설정
        try {
            contentType = Files.probeContentType(path);
        } catch (Exception e) {
            log.error("Failed to determine file type", e);
        }

        // 이미지 반환 (ContentType 지정)
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType)) // 파일의 Content-Type 설정
                .body(resource);
    }


}
