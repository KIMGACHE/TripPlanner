package com.tripPlanner.project.domain.signin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

<<<<<<< HEAD
=======
import java.time.LocalDate;
import java.time.LocalDateTime;

>>>>>>> 이영훈
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserDto {

    private String userid;
    private String img;
    private String username;
    private String password;
    private String repassword;
    private String email;
<<<<<<< HEAD
    private int birth;
=======
    private LocalDate birth;
>>>>>>> 이영훈
    private String gender;
    private String role;
}
