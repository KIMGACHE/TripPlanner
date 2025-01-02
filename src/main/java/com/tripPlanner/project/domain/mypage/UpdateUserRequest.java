package com.tripPlanner.project.domain.mypage;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String username;
    private String img;
    private String email;
    private String password;
    private String repassword;
}