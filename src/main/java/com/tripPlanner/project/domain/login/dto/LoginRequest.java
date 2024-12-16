package com.tripPlanner.project.domain.login.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {
    private String userid;
    private String password;
    private String role;

    private String provider;
    private String providerId;

}
