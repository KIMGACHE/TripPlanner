package com.tripPlanner.project.domain.signin;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private String userid;
    private String img;
    private String username;
    private String password;
    private String Email;
    private String addr;
    private String birth;

}

