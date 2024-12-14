package com.tripPlanner.project.domain.login.auth.provider;


import java.util.Map;

public interface Oauth2Userinfo {

    String getName();
    String getEmail();
    String getProvider();
    String getProviderId();
    Map<String, Object> getAttributes();
}
