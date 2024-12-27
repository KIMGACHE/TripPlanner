//package com.tripPlanner.project.commons.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Entity
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@Data
//@Table(name = "tbl_user")
//public class UserEntity {
//
//    @Id
//    @Column(name = "userid", length = 20)
//    private String userid;
//
//    @Column(name = "img")
//    private String img;
//
//    @Column(name = "username", nullable = false)
//    private String username;
//
//    @Column(name = "password", nullable = false)
//    private String password;
//
//    @Column(name = "email", nullable = false)
//    private String email;
//
//    @Column(name = "birth", nullable = false ,length = 8)
//    private int birth;
//
//    @Column(name = "gender", nullable = false)
//    private String gender;
//
//    @Column(name = "role", nullable = false)
//    private String role;
//
//    @PrePersist // 엔티티 저장 직전에 호출
//    public void prePersist() {
//        this.role = this.role == null ? "ROLE_USER" : this.role;
//    }
//}

package com.tripPlanner.project.commons.entity;

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
