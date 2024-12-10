package com.tripPlanner.project.domain.login;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {
    private String userid;
    private String password;
    private String username;


}
