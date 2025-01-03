package com.tripPlanner.project.domain.signin;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "tbl_user")
public class UserEntity {

    @Id
    @Column(name = "userid")
    private String userid;

    @Column(name = "img")
    private String img;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "role", nullable = false)
    private String role;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String birth;

    private String provider;
    private String providerId;

    @PrePersist // 엔티티 저장 직전에 호출
    public void prePersist() {
        this.role = this.role == null ? "ROLE_USER" : this.role;
    }
}
