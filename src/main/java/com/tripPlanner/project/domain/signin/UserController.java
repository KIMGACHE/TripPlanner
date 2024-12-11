package com.tripPlanner.project.domain.signin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/join")
    public void join() {
        log.info("Get /join...");// join.html을 반환
    }

    @PostMapping("/join")
    public String join_post(@RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
                            @RequestParam Map<String, String> formData, UserDto userDto) throws IOException {
        log.info("Post /join " + userDto);

        userDto.setRepassword(formData.get("repassword"));
        String error = userService.joinUser(userDto, profileImage);

        if (error != null) {
            log.error("Error occurred during user registration: {}", error);
            return "redirect:/user/join?error=" + error;
        }

        return "redirect:/"; // 회원가입 성공 후 홈 화면으로 리다이렉트
    }

    // 아이디 유효성 검사
    @PostMapping("/check-id")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkUserId(@RequestBody Map<String, String> data) {
        String userid = data.get("userid");
        String result = userService.validateUserId(userid);

        Map<String, Object> response = new HashMap<>();
        if (result == null) {
            response.put("available", true);
            response.put("message", "사용 가능한 아이디입니다.");
        } else {
            response.put("available", false);
            response.put("message", result);
        }

        return ResponseEntity.ok(response);
    }
}
