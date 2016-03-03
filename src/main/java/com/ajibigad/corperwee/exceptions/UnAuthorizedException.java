package com.ajibigad.corperwee.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.function.UnaryOperator;

/**
 * Created by Julius on 02/03/2016.
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "You are not authorized to perform this action")
public class UnAuthorizedException extends RuntimeException {

    private String username;

    public UnAuthorizedException(String username){
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
