package com.tripPlanner.project.domain.signin;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
<<<<<<< HEAD
=======
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
>>>>>>> 이영훈

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "tbl_user")
public class UserEntity {

    @Id
<<<<<<< HEAD
    @Column(name = "userid", length = 20)
=======
    @Column(name = "userid")
>>>>>>> 이영훈
    private String userid;

    @Column(name = "img")
    private String img;

    @Column(name = "username", nullable = false)
    private String username;

<<<<<<< HEAD
    @Column(name = "password", nullable = false)
=======
    @Column(name = "password")
>>>>>>> 이영훈
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

<<<<<<< HEAD
    @Column(name = "birth", nullable = false ,length = 8)
    private int birth;

=======
>>>>>>> 이영훈
    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "role", nullable = false)
    private String role;

<<<<<<< HEAD
=======
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;

    private String provider;
    private String providerId;

>>>>>>> 이영훈
    @PrePersist // 엔티티 저장 직전에 호출
    public void prePersist() {
        this.role = this.role == null ? "ROLE_USER" : this.role;
    }
}
