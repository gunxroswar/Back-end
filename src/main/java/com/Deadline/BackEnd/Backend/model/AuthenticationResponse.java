package com.Deadline.BackEnd.Backend.model;

public class AuthenticationResponse {
    private String token,profileName;
    public AuthenticationResponse(String token,String profileName){
        this.profileName = profileName;
        this.token = token;
    }

    public String getToken(){
        return token;
    }
    public String getProfileName(){
        return profileName;
    }
}
