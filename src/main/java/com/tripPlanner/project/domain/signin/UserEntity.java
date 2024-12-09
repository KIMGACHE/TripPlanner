package com.tripPlanner.project.domain.signin;

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
    @Column(name = "userid" , length = 20)
    private String userid;
    @Column(name = "img")
    private String img;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @Column(name = "addr")
    private String addr;
    @Column(name = "birth")
    private String birth;
}
