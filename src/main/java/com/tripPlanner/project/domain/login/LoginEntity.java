package com.tripPlanner.project.domain.login;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name= "logininfo")
@Data
@AllArgsConstructor
@Builder
public class LoginEntity {
    @Id
    @Column(name="userid" ,nullable=false)
    private String userid;

    @Column(name="password" ,nullable=false)
    private String password;

    public static LoginEntity toEntity(LoginDto dto){
        return LoginEntity.builder()
                .userid(dto.getUserid())
                .password(dto.getPassword())
                .build();
    }
}
