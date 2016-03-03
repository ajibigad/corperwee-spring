package com.ajibigad.corperwee.exceptions;

/**
 * Created by Julius on 23/02/2016.
 */
public class UserNotFoundException extends RuntimeException {

    private String username;
    private long userId;

    public UserNotFoundException(long user_id, String username){
        this.userId = userId;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(int user_id) {
        this.userId = userId;
    }
}
