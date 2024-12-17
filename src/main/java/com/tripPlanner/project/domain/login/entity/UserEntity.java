package com.tripPlanner.project.domain.login.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name="user")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    private String userid;
    private String username;
    private String email;
    private String password;
    private String profileImg;
    private String addr;
    private String role;
    private char gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;

    private String provider;
    private String providerId;

}
