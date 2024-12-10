package com.tripPlanner.project.domain.user;

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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto Increment ID값
    private Long id;

    @Column(unique = true) //중복은 안되게
    private String userid;
    private String username;
    private String email;
    private String password;
    private String profileImg;
    private String addr;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate birth;
}
