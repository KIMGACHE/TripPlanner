package com.tripPlanner.project.domain.Mypage;

import com.tripPlanner.project.domain.login.auth.jwt.JwtTokenProvider;
import com.tripPlanner.project.domain.signin.UserDto;
import com.tripPlanner.project.domain.signin.UserEntity;
import com.tripPlanner.project.domain.signin.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MypageService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public UserDto getUserInfo(String token) {

        System.out.println("Token: " + token); // token 출력 확인

        String userid = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출
        System.out.println("Service Userid : " + userid); // 사용자 ID 출력

        if (userid == null) {
            throw new IllegalArgumentException("Invalid token or no user ID found.");
        }
        UserEntity userEntity = userRepository.findById(userid)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        System.out.println("Service Userid :"+userid);
        return new UserDto(
                userEntity.getUserid(),
                userEntity.getImg(),
                userEntity.getUsername(),
                null, // 비밀번호 제외
                null,
                userEntity.getEmail(),
                userEntity.getBirth(),
                userEntity.getGender(),
                userEntity.getRole()
        );
    }


//    public List<String> getUserPlanners(String token) {
//        String userid = jwtTokenProvider.getUserIdFromToken(token);
//        // 예시 데이터 (실제 DB 로직 적용)
//        return List.of("Planner 1", "Planner 2", "Planner 3");
//    }
//
//    public List<String> getLikedPlanners(String token) {
//        String userid = jwtTokenProvider.getUserIdFromToken(token);
//        // 예시 데이터 (실제 DB 로직 적용)
//        return List.of("Liked Planner 1", "Liked Planner 2");
//    }
}
