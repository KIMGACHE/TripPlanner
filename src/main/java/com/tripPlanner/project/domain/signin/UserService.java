package com.tripPlanner.project.domain.signin;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String joinUser(UserDto userDto, MultipartFile profileImage) throws IOException {
        // 비밀번호가 null 또는 빈 값인지 확인
        if (userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
            return "password";
        }

        // 비밀번호 확인 (repassword) 처리
        if (!userDto.getPassword().equals(userDto.getRepassword())) {
            return "passwordMismatch";
        }

        // 아이디 유효성 검사
        String idValidationResult = validateUserId(userDto.getUserid());
        if (idValidationResult != null) {
            return idValidationResult; // 유효하지 않으면 에러 리턴
        }

        // 업로드된 프로필 이미지 저장
        String profileImgPath = saveProfileImage(userDto.getUserid(), profileImage);

        // 사용자 정보를 저장
        UserEntity user = UserEntity.builder()
                .userid(userDto.getUserid())
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword())) // 암호화
                .email(userDto.getEmail())
                .addr(userDto.getAddr())
                .birth(userDto.getBirth())
                .img(profileImgPath)
                .build();

        userRepository.save(user);
        return null; // 회원가입 성공
    }

    // 아이디 유효성 검사
    public String validateUserId(String userid) {
        // 아이디는 1자리 이상 10자리 이하, 특수문자 및 띄어쓰기 제외
        if (userid.length() < 1 || userid.length() > 10) {
            return "아이디는 1자리 이상 10자리 이하로 입력해주세요.";
        }else if (!userid.matches("^[a-zA-Z0-9]+$")) {
            return "아이디는 특수문자와 띄어쓰기를 사용할 수 없습니다.";
        }

        // 아이디 중복 검사
        Optional<UserEntity> existingUser = userRepository.findByUserid(userid);
        if (existingUser.isPresent()) {
            return "이미 사용 중인 아이디입니다.";
        }

        return null; // 유효한 아이디
    }

    private String saveProfileImage(String userId, MultipartFile profileImage) throws IOException {
        // 프로필 이미지 저장 로직
        UploadProperties uploadProperties = new UploadProperties();
        String dirPath = UploadProperties.uploadPath
                + File.separator
                + UploadProperties.profilePath
                + File.separator
                + userId;

        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String profileImgPath = "profileImg.jpg";
        if (profileImage != null && !profileImage.isEmpty()) {
            File profileFile = new File(dirPath + File.separator + profileImage.getOriginalFilename());
            profileImage.transferTo(profileFile);
            profileImgPath = profileImage.getOriginalFilename();
        } else {
            String defaultImagePath = "src/main/resources/static/assets/anonymous.jpg";
            File defaultImage = new File(defaultImagePath);
            File copyDefaultImage = new File(dirPath + File.separator + profileImgPath);
            if (defaultImage.exists()) {
                Files.copy(defaultImage.toPath(), copyDefaultImage.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        }
        return profileImgPath;
    }
}
