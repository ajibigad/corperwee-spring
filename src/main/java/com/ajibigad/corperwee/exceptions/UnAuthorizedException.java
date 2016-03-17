package com.ajibigad.corperwee.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by Julius on 02/03/2016.
 */
public class UnAuthorizedException extends RuntimeException {

    @JsonIgnore
    private String username;

    private String message = "Username : " + SecurityContextHolder.getContext().getAuthentication().getName() + " is not authorized to perform this action";

    public String getMessage(){
        return this.message;
    }
}
