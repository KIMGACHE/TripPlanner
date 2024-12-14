package com.tripPlanner.project.domain.login.auth;

import com.tripPlanner.project.domain.login.LoginRequest;
import com.tripPlanner.project.domain.user.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class PrincipalDetail implements UserDetails, OAuth2User {

    private LoginRequest loginRequest;
    private Map<String, Object> attributes;

    public PrincipalDetail(UserEntity userEntity,Map<String,Object> attributes){
        this.loginRequest = loginRequest;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return loginRequest.getPassword();
    }

    @Override
    public String getUsername() {
        return loginRequest.getUserid();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    @Override
    public String getName() {
        return null;
    }

}
