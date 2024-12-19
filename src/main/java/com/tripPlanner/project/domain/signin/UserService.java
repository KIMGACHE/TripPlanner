package com.tripPlanner.project.domain.signin;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j // Logger 사용
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender emailSender;
    private final ConcurrentHashMap<String, String> authCodes = new ConcurrentHashMap<>();

    private final ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();

    @PostConstruct
    public void init() {
        taskScheduler.initialize();
    }

    // 회원가입 처리
    public String joinUser(UserDto userDto) throws Exception {
        // 비밀번호 확인
        if (!userDto.getPassword().equals(userDto.getRepassword())) {
            return "비밀번호가 일치하지 않습니다.";
        }

        // 아이디 중복 검사
        if (userRepository.findByUserid(userDto.getUserid()).isPresent()) {
            return "이미 사용 중인 아이디입니다.";
        }

        // 이메일 중복 검사
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            return "이미 등록된 이메일입니다.";
        }

        // 이미지가 없을 경우 기본 이미지 설정
        if (userDto.getImg() == null || userDto.getImg().trim().isEmpty()) {
            userDto.setImg("/uploads/basic/anonymous.jpg");
        }

        // 비밀번호 암호화 후 저장
        UserEntity user = UserEntity.builder()
                .userid(userDto.getUserid())
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .birth(userDto.getBirth())
                .img(userDto.getImg()) // 기본 이미지 설정 확인
                .role(userDto.getRole())
                .gender(userDto.getGender())
                .build();

        userRepository.save(user);
        log.info("UserID : {}", userDto.getUserid());
        log.info("UserPassword : {}", userDto.getPassword());
        log.info("UserImage : {}", userDto.getImg());

        return null; // 성공 시 null 반환
    }


    // 이미지 업로드 처리 (기본 이미지 설정 추가)
    public String uploadProfileImage(String userid, MultipartFile file) throws IOException {
        String defaultImagePath = "/planner/ProfileImg/anonymous.jpg"; // 기본 이미지 경로

        if (file == null || file.isEmpty()) {
            log.info("파일이 비어있어 기본 이미지를 사용합니다.");
            return defaultImagePath; // 기본 이미지 반환
        }

        String folderPath = UploadProperties.uploadPath + "/" + UploadProperties.profilePath + "/" + userid;
        File dir = new File(folderPath);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        String originalFilename = file.getOriginalFilename();
        String savedFileName = System.currentTimeMillis() + "_" + originalFilename;
        File dest = new File(dir, savedFileName);
        file.transferTo(dest);

        log.info("이미지 저장 완료: {}", dest.getAbsolutePath());

        return "/uploads/" + UploadProperties.profilePath + "/" + userid + "/" + savedFileName;
    }

    // 이메일 인증번호 생성 및 발송
    public String sendAuthCode(String email) {
        String authCode = generateAuthCode();
        authCodes.put(email, authCode);

        // 3분 후 인증 코드 삭제
        taskScheduler.schedule(() -> {
            authCodes.remove(email);
            log.info("인증 코드 삭제: {}", email);
        }, new Date(System.currentTimeMillis() + 3 * 60 * 1000)); // 3분(3*60*1000) 후 실행

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("회원가입 인증 코드");
        message.setText("인증 코드: " + authCode);

        try {
            emailSender.send(message);
            log.info("인증 코드 발송: {}", authCode);
            return "success";
        } catch (Exception e) {
            log.error("인증 코드 발송 실패: {}", e.getMessage());
            return "fail";
        }
    }

    // 인증번호 확인
    public String verifyAuthCode(String email, String code) {
        if (authCodes.containsKey(email) && authCodes.get(email).equals(code)) {
            authCodes.remove(email);
            return "인증이 완료되었습니다.";
        }
        return "올바른 코드가 아닙니다. 다시 확인해주세요.";
    }

    // 인증번호 재전송
    public String resendAuthCode(String email) {
        if (!authCodes.containsKey(email)) {
            return "인증 요청이 없습니다.";
        }
        return sendAuthCode(email);
    }

    // 랜덤 인증번호 생성
    private String generateAuthCode() {
        Random random = new Random();
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int idx = random.nextInt(3);
            switch (idx) {
                case 0: key.append((char) (random.nextInt(26) + 97)); break; // 소문자
                case 1: key.append((char) (random.nextInt(26) + 65)); break; // 대문자
                case 2: key.append(random.nextInt(10)); break; // 숫자
            }
        }
        return key.toString();
    }
}
