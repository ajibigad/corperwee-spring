package com.ajibigad.corperwee.model.apiModels;

/**
 * Created by Julius on 21/03/2016.
 */
public class PasswordReset {

    private long userId;
    private String token;
    private String password;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
