package com.tripPlanner.project.commons.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "tbl_user")
public class UserEntity {

    @Id
    @Column(name = "userid", length = 20)
    private String userid;

    @Column(name = "img")
    private String img;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "birth", nullable = false ,length = 8)
    private int birth;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "role", nullable = false)
    private String role;

    @PrePersist // 엔티티 저장 직전에 호출
    public void prePersist() {
        this.role = this.role == null ? "ROLE_USER" : this.role;
    }
}