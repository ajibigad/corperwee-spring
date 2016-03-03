package com.ajibigad.corperwee.exceptions;

/**
 * Created by Julius on 25/02/2016.
 */
public class UserExistAlready extends RuntimeException {

    private String username;

    public UserExistAlready(String username){
        //this.userId = userId;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
