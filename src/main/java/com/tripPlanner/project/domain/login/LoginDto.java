package com.tripPlanner.project.domain.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDto {
    private String userid;
    private String password;

    public static LoginDto toDto(LoginEntity entity){
        return LoginDto.builder()
                .userid(entity.getUserid())
                .password(entity.getPassword())
                .build();
    }
}
