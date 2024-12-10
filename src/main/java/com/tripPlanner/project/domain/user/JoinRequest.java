package com.tripPlanner.project.domain.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinRequest {

    private String loginId;
    private String password;
    private String passwordCheck;

    private String email;
}
