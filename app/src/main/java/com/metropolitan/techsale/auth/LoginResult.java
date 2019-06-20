package com.metropolitan.techsale.auth;

public class LoginResult {

    private String username;
    private String token;

    public LoginResult() {
    }

    public LoginResult(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
