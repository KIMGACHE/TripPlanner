package com.tripPlanner.project.domain.mypage.Service;

import com.tripPlanner.project.domain.mypage.UpdateUserRequest;
import org.springframework.stereotype.Service;

@Service
public class MypageService {

    public void validatePassword(String password) {
        String passwordRegex = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]{8,15}$";
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("비밀번호를 입력해주세요.");
        }
        if (password.contains(" ")) {
            throw new IllegalArgumentException("비밀번호에 공백은 사용할 수 없습니다.");
        }
        if (!password.matches(passwordRegex)) {
            throw new IllegalArgumentException("비밀번호는 영문+숫자 조합, 8~15자리여야 합니다.");
        }
    }

    public void validateEmail(String email) {
        String emailRegex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("이메일을 입력해주세요.");
        }
        if (!email.matches(emailRegex)) {
            throw new IllegalArgumentException("유효한 이메일 형식을 입력하세요.");
        }
    }

    public void validateUsername(String username) {
        String usernameRegex = "^[a-zA-Z가-힣]+$";
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("사용자 이름을 입력해주세요.");
        }
        if (!username.matches(usernameRegex)) {
            throw new IllegalArgumentException("이름은 영어 및 한글만 사용할 수 있습니다.");
        }
    }

    public void validateUpdateRequest(UpdateUserRequest request) {
        if (request.getUsername() != null) {
            validateUsername(request.getUsername());
        }
        if (request.getEmail() != null) {
            validateEmail(request.getEmail());
        }
        if (request.getPassword() != null) {
            validatePassword(request.getPassword());
        }
    }
}
