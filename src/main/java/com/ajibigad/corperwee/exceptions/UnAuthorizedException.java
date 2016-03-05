package com.ajibigad.corperwee.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Julius on 02/03/2016.
 */
public class UnAuthorizedException extends RuntimeException {

    @JsonIgnore
    @Value("#{principal.username}")
    private String username;

    private String message = "Username : " + username + " is not authorized to perform this action";

    public String getMessage(){
        return this.message;
    }
}
